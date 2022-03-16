# Sequential SSH Commands Rundeck Plugin

This is a Node Step Job plugin that sends commands to a remote node via SSH. The commands occur in the same SSH channel, and therefore share state.  This is particularly useful for network devices, where you might use the command `conf t` followed by a series of commands such as `interface vlan 1` and `ip address 1.1.1.1 255.255.255.0` and `no shut`.  Use-cases for compute resources would be if you could only execute certain commands from a particular working directory or with certain environment variables set.

![Screen Shot 2021-12-30 at 3 55 39 PM](https://user-images.githubusercontent.com/11511251/147795129-b5a593ec-82e8-4acd-a25b-69270fd8c55a.png)

# Build
Run the following command to built the jar file:

`./gradlew clean build`

# Install
Copy the `sequential-commands-plugin-x.y.x.jar` file to the `$RDECK_BASE/libext/` directory inside your Rundeck installation.

Or you can upload the jar file as described [here](https://docs.rundeck.com/docs/learning/howto/calling-apis.html#community-version-prerequisite).

# Usage
The plugin makes use of SSH credentials that are set on the nodes in Rundeck. If the `ssh-password-storage-path` attribute is set, then the plugin will authenticate using the SSH password, otherwise it will use the `ssh-key-storage-path` attribute.

To add a command to the Job Step, click on **`Add Custom Field`**:
![Screen Shot 2022-03-16 at 11 03 31 AM](https://user-images.githubusercontent.com/11511251/158657441-0dc90855-fe4f-461b-a20b-9d5a1968ade6.png)

In the configuration popup, set the field `Field Label` and `Field Key`. Inputs here **do not** influence the command that is sent to the remote node - they serve to identify the commands within the Job Step.  Optionally set the `Description` field:
![Screen Shot 2022-03-16 at 11 10 58 AM](https://user-images.githubusercontent.com/11511251/158658778-aa5636a3-1c84-4c5f-a8a4-3e8e3cbe5c07.png)

This then adds a field within the Job Step where you can input a command that you want to send to the remote node. Continue to add _Custom Fields_ to the Job step such that you can send multiple commands to the node in a single SSH channel. Commands are sent to the remote node in sequential order. As an example, in the screenshot below, the order of commands send to the remote node would be: `enable`, then a secure-job-option containing the _enable password_ is sent to the device (this will not be shown in the logs output), this is then followed by the `terminal length 0` command and then finally the `show interfaces` command.
![Screen Shot 2022-03-16 at 11 20 26 AM](https://user-images.githubusercontent.com/11511251/158660331-b6771155-8765-44bd-a752-53e31ec825cb.png)

