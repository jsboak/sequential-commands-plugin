package com.plugin.sequentialcommands;

import com.dtolabs.rundeck.core.common.INodeEntry;
import com.dtolabs.rundeck.core.execution.workflow.steps.FailureReason;
import com.dtolabs.rundeck.core.execution.workflow.steps.StepException;
import com.dtolabs.rundeck.core.execution.workflow.steps.node.NodeStepException;
import com.dtolabs.rundeck.core.plugins.Plugin;
import com.dtolabs.rundeck.core.plugins.configuration.Describable;
import com.dtolabs.rundeck.core.plugins.configuration.Description;
import com.dtolabs.rundeck.core.plugins.configuration.StringRenderingConstants;
import com.dtolabs.rundeck.plugins.ServiceNameConstants;
import com.dtolabs.rundeck.plugins.descriptions.PluginDescription;
import com.dtolabs.rundeck.plugins.step.PluginStepContext;
import com.dtolabs.rundeck.plugins.util.DescriptionBuilder;
import com.dtolabs.rundeck.plugins.util.PropertyBuilder;
import com.dtolabs.rundeck.plugins.PluginLogger;
import com.dtolabs.rundeck.plugins.step.NodeStepPlugin;
import com.google.gson.*;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Map;


@Plugin(service=ServiceNameConstants.WorkflowNodeStep,name="sequential-commands")
@PluginDescription(title="sequential-commands", description="Send multiple commands in a single SSH Session. Particularly useful for network devices.")
public class Sequentialcommands implements NodeStepPlugin, Describable{

    public static final String SERVICE_PROVIDER_NAME = "sequential-commands";

   /**
     * Overriding this method gives the plugin a chance to take part in building the {@link
     * com.dtolabs.rundeck.core.plugins.configuration.Description} presented by this plugin.  This subclass can use the
     * {@link DescriptionBuilder} to modify all aspects of the description, add or remove properties, etc.
     */
   @Override
   public Description getDescription() {
        return DescriptionBuilder.builder()
            .name(SERVICE_PROVIDER_NAME)
            .title("Sequential SSH Commands")
            .description("Send multiple commands in a single SSH Session. Particularly useful for network devices.")
            .property(PropertyBuilder.builder()
                    .string("custom")
                    .title("Commands")
                    .description("Use \"Add Custom Field\" to add commands you want to send.\n" +
                            "The Label is a user-friendly descriptor for the command.\n" +
                            "The key is a unique identifier of your choosing for that command.\n" +
                            "Once the custom field is created, input your command into the field's textarea.")
                    .renderingOption(StringRenderingConstants.DISPLAY_TYPE_KEY, "DYNAMIC_FORM")
                    .build()
            )
            .build();
   }

   /**
     * This enum lists the known reasons this plugin might fail
     */
   static enum Reason implements FailureReason{
        ExampleReason
   }

      @Override
      public void executeNodeStep(final PluginStepContext context,
                                  final Map<String, Object> configuration,
                                  final INodeEntry entry) throws NodeStepException {

          String userName = entry.getAttributes().get("username");
          String hostname = entry.getAttributes().get("hostname");
          String sshKeyStoragePath;
          boolean usePrivKey;
          String sshPrivKey = null;

          if (entry.getAttributes().get("ssh-password-storage-path") != null) {
              sshKeyStoragePath = entry.getAttributes().get("ssh-password-storage-path");
              usePrivKey = false;
          } else {
              sshKeyStoragePath = entry.getAttributes().get("ssh-key-storage-path");
              usePrivKey = true;
          }

          try {
              sshPrivKey = PluginUtil.getPasswordFromKeyStorage(sshKeyStoragePath, context);
          } catch (StepException e) {
              e.printStackTrace();
          }

          Gson gson = new GsonBuilder().create();
          JsonArray customFields = gson.fromJson(configuration.get("custom").toString(), JsonArray.class);

          if(customFields != null ) {

              try {

                  SSHConnect connection = new SSHConnect(userName,sshPrivKey,hostname,usePrivKey);
                  Session session = connection.connect();

                  Channel channel = session.openChannel("shell");
                  OutputStream ops = channel.getOutputStream();
                  PrintStream ps = new PrintStream(ops, true);
                  channel.connect();
                  InputStream input = channel.getInputStream();

                  for (JsonElement customField : customFields) {

                      JsonObject cmdJson = customField.getAsJsonObject();
                      String value = cmdJson.get("value").toString();
                      String rawCommand = value.substring(1, value.length()-1);

                      ps.println(rawCommand);
                      Thread.sleep(500);

                  }

                  ps.close();
                  SSHConnect.printResult(input, channel);

                  channel.disconnect();
                  session.disconnect();

              } catch (Exception e) {
                  e.printStackTrace();
              }
          }
      }
}