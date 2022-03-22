package com.plugin.sequentialcommands;

import com.dtolabs.rundeck.core.common.INodeEntry;
import com.dtolabs.rundeck.core.execution.workflow.steps.node.NodeStepException;
import com.dtolabs.rundeck.core.storage.ResourceMeta;
import com.dtolabs.rundeck.plugins.step.PluginStepContext;
import java.io.ByteArrayOutputStream;

/*
Created by Jake Cohen on 3/20/2022
This Rundeck plugin is used to send sequential commands via SSH.
 */
public class PluginUtil {

    public static String getPasswordFromKeyStorage(String path, PluginStepContext context, INodeEntry node) throws NodeStepException {
        try{
            ResourceMeta contents = context.getExecutionContext().getStorageTree().getResource(path).getContents();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            contents.writeContent(byteArrayOutputStream);

            return byteArrayOutputStream.toString();
        }catch(Exception e){
            throw new NodeStepException("error accessing " + path + " from Key Storage: no resource for path", Sequentialcommands.Reason.KeyStorage, node.getNodename());
        }
    }

}
