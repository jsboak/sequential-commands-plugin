# Sequential SSH Commands Rundeck Plugin

This is a Node Step Job plugin that sends commands to a remote node via SSH. The commands occur in the same SSH channel, and therefore share state.  This is particularly useful for network devices (see example below) or for compute resources when certain commands can strictly be executed from a particular working directory or with certain environment variables set.

![Screen Shot 2021-12-30 at 3 55 39 PM](https://user-images.githubusercontent.com/11511251/147795129-b5a593ec-82e8-4acd-a25b-69270fd8c55a.png)

## Build
Run the following command to build the jar file (using Java 1.8):

`gradle build`

Alternatively download the latest released Jar from the [Releases Page](https://github.com/jsboak/sequential-commands-plugin/releases)

## Install
Copy the `sequential-commands-plugin-x.y.x.jar` file to the `$RDECK_BASE/libext/` directory inside your Rundeck installation.

Or you can upload the file through the GUI as described [here](https://docs.rundeck.com/docs/learning/howto/calling-apis.html#community-version-prerequisite).

## Usage
The plugin makes use of SSH credentials that are set on the nodes in Rundeck. If the `ssh-password-storage-path` attribute is set, then the plugin will authenticate using the SSH password, otherwise it will use the `ssh-key-storage-path` attribute.

To add a command to the Job Step, click on **`Add Custom Field`**:

![Screen Shot 2022-03-16 at 11 03 31 AM](https://user-images.githubusercontent.com/11511251/158657441-0dc90855-fe4f-461b-a20b-9d5a1968ade6.png)

In the configuration popup, set the field `Field Label` and `Field Key`. Inputs here **do not** influence the command that is sent to the remote node - they serve to identify the commands within the Job Step.  Optionally set the `Description` field:

![Screen Shot 2022-03-16 at 11 10 58 AM](https://user-images.githubusercontent.com/11511251/158658778-aa5636a3-1c84-4c5f-a8a4-3e8e3cbe5c07.png)

This then adds a field within the Job Step where you can input a command that you want to send to the remote node. Continue to add _Custom Fields_ to the Job step such that you can send multiple commands to the node in a single SSH channel. Commands are sent to the remote node in sequential order. 

## Example
As an example, in the screenshot below, the order of commands send to the remote node would be: `enable`, then a secure-job-option containing the _enable password_ is sent to the device (this will not be shown in the logs output), this is then followed by the `terminal length 0` command and then finally the `show interfaces` command. 
You can view and download this example job [here](https://github.com/jsboak/sequential-commands-plugin/blob/main/example-jobs/Cisco_Router_-_Show_Interfaces.yaml).

![Screen Shot 2022-03-16 at 11 20 26 AM](https://user-images.githubusercontent.com/11511251/158660331-b6771155-8765-44bd-a752-53e31ec825cb.png)

The output for this example (on a Cisco CSR) would appear like so:

![Screen Shot 2022-03-16 at 11 27 20 AM](https://user-images.githubusercontent.com/11511251/158661436-391f4134-b96d-4606-898f-34938d9ccad1.png)

Optionally select the **`Strict Host Key Checking`** checkbox to choose whether or not Rundeck checks that the remote-node is in the `known_hosts` file.
