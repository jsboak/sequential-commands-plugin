package com.plugin.sequentialcommands

import com.dtolabs.rundeck.core.common.INodeDesc
import com.dtolabs.rundeck.core.execution.ExecutionContext
import com.dtolabs.rundeck.core.execution.ExecutionListener
import com.dtolabs.rundeck.core.execution.workflow.steps.node.NodeStepException
import com.dtolabs.rundeck.plugins.step.PluginStepContext
import com.dtolabs.rundeck.core.execution.workflow.steps.StepException
import com.dtolabs.rundeck.plugins.PluginLogger
import com.dtolabs.rundeck.core.common.INodeEntry
import com.dtolabs.rundeck.core.common.Framework
import com.dtolabs.rundeck.core.storage.keys.KeyStorageTree
import spock.lang.Specification

class SequentialcommandsSpec extends Specification {

    def getContext(PluginLogger logger){
        Mock(PluginStepContext){
            getLogger()>>logger
        }
    }

    def "Check SSH Key path"() {

        given:
        def Sequential = new Sequentialcommands()
        def storageTree = Mock(KeyStorageTree)
        storageTree.getResource("keys/123") >> { throw NodeStepException(e);}
        def executionListener = Mock(ExecutionListener)
        def executionContext = Mock(ExecutionContext){
            getExecutionListener() >> executionListener
            getStorageTree() >> storageTree

        }
        INodeEntry node = new INodeEntry() {
            @Override
            String getOsFamily() {
                return null
            }

            @Override
            String getOsArch() {
                return null
            }

            @Override
            String getOsVersion() {
                return null
            }

            @Override
            String getOsName() {
                return null
            }

            @Override
            String getNodename() {
                return null
            }

            @Override
            String getUsername() {
                return null
            }

            @Override
            boolean containsUserName() {
                return false
            }

            @Override
            boolean containsPort() {
                return false
            }

            @Override
            String extractUserName() {
                return null
            }

            @Override
            String extractHostname() {
                return null
            }

            @Override
            String extractPort() {
                return null
            }

            @Override
            String getFrameworkProject() {
                return null
            }

            @Override
            String getDescription() {
                return null
            }

            @Override
            Set getTags() {
                return null
            }

            @Override
            Map<String, String> getAttributes() {
                return ["nodename":"myDevice","username":"myUserName","ssh-password-storage-path":"keys/ssh-pass"]
            }

            @Override
            String getHostname() {
                return null
            }

            @Override
            boolean equals(INodeDesc n) {
                return false
            }
        }
        def context = Mock(PluginStepContext) {
            getExecutionContext() >> executionContext
            getLogger() >> Mock(PluginLogger)
        }
        def configuration = [region:"us-west-1",accountId:"1234567890"]
        when:
        Sequential.executeNodeStep(context,configuration,node)

        then:
        NodeStepException e = thrown()
        e.message == "error accessing keys/ssh-pass from Key Storage: no resource for path"

    }

    def "Check SSH Key Node Attribute"() {

        given:
        def Sequential = new Sequentialcommands()
        def context = Mock(PluginStepContext){
            getFramework() >> Mock(Framework)
        }
        INodeEntry node = new INodeEntry() {
            @Override
            String getOsFamily() {
                return null
            }

            @Override
            String getOsArch() {
                return null
            }

            @Override
            String getOsVersion() {
                return null
            }

            @Override
            String getOsName() {
                return null
            }

            @Override
            String getNodename() {
                return null
            }

            @Override
            String getUsername() {
                return null
            }

            @Override
            boolean containsUserName() {
                return false
            }

            @Override
            boolean containsPort() {
                return false
            }

            @Override
            String extractUserName() {
                return null
            }

            @Override
            String extractHostname() {
                return null
            }

            @Override
            String extractPort() {
                return null
            }

            @Override
            String getFrameworkProject() {
                return null
            }

            @Override
            String getDescription() {
                return null
            }

            @Override
            Set getTags() {
                return null
            }

            @Override
            Map<String, String> getAttributes() {
                return ["nodename":"myDevice","username":"myUserName"]
            }

            @Override
            String getHostname() {
                return null
            }

            @Override
            boolean equals(INodeDesc n) {
                return false
            }
        }
        def configuration = [region:"us-west-1",accountId:"1234567890"]
        when:
        Sequential.executeNodeStep(context,configuration,node)

        then:
        NodeStepException e = thrown()
        e.message == "SSH Key or Password must be defined as node attribute."

    }

    def "check Boolean parameter"(){

        given:

        def example = new Sequentialcommands()
        def context = getContext(Mock(PluginLogger))
        def node = Mock(INodeEntry){
                        getNodename()>>"Test"
                        getAttributes()>>["attr:name":"Test"]
                    }

        def configuration = [example:"example123",exampleBoolean:"true"]

        when:
        example.executeNodeStep(context,configuration,node)

        then:
        thrown StepException
    }

//    def "run OK"(){
//
//        given:
//
//        def example = new Sequentialcommands()
//        def logger = Mock(PluginLogger)
//        def context = getContext(logger)
//        def node = Mock(INodeEntry){
//                        getNodename()>>"Test"
//                        getAttributes()>>["attr:name":"Test"]
//                    }
//
//        def configuration = [example:"example123",exampleBoolean:"false"]
//
//        when:
//        example.executeNodeStep(context,configuration,node)
//
//        then:
//        1 * logger.log(2, "Example node step executing on node: Test")
//    }

}