# Sequential SSH Commands Rundeck Plugin

Use this plugin to send multiple commands to the comandline of remote devices via SSH in a single Job step. This is especially useful for managing network devices that require multiple commands in a single SSH session.

![Screen Shot 2021-12-30 at 3 55 39 PM](https://user-images.githubusercontent.com/11511251/147795129-b5a593ec-82e8-4acd-a25b-69270fd8c55a.png)

# Build
Run the following command to built the jar file:

`./gradlew clean build`

# Install
Copy the `SEQUENTIAL-COMMANDS-x.y.x.jar` file to the `$RDECK_BASE/libext/` directory inside your Rundeck installation.

Or you can upload the jar file as described [here](https://docs.rundeck.com/docs/learning/howto/calling-apis.html#community-version-prerequisite).
