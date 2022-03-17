package com.plugin.sequentialcommands;

import com.dtolabs.rundeck.core.common.INodeEntry;
import com.dtolabs.rundeck.core.execution.workflow.steps.StepException;
import com.dtolabs.rundeck.core.execution.workflow.steps.node.NodeStepException;
import com.dtolabs.rundeck.core.storage.ResourceMeta;
import com.dtolabs.rundeck.plugins.step.PluginStepContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PluginUtil {

    public static String getPasswordFromKeyStorage(String path, PluginStepContext context, INodeEntry node) throws NodeStepException {
        try{
            ResourceMeta contents = context.getExecutionContext().getStorageTree().getResource(path).getContents();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            contents.writeContent(byteArrayOutputStream);

            return byteArrayOutputStream.toString();
        }catch(Exception e){
            throw new NodeStepException("error accessing " + path + " from Key Storage: no resource for path", Sequentialcommands.Reason.KeyStorage, node.getNodename());
//            return "Error accessing ${path} from Key Storage.";
        }
    }

}
