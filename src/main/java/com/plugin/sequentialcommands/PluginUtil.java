package com.plugin.sequentialcommands;

import com.dtolabs.rundeck.core.execution.workflow.steps.StepException;
import com.dtolabs.rundeck.core.storage.ResourceMeta;
import com.dtolabs.rundeck.plugins.step.PluginStepContext;

import java.io.ByteArrayOutputStream;

public class PluginUtil {

    public static String getPasswordFromKeyStorage(String path, PluginStepContext context) throws StepException {
        try{
            ResourceMeta contents = context.getExecutionContext().getStorageTree().getResource(path).getContents();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            contents.writeContent(byteArrayOutputStream);

            return byteArrayOutputStream.toString();
        }catch(Exception e){
            return "Error accessing ${path} from Key Storage.";
        }
    }

}
