# Che end-to-end tests
Che end-to-end tests verify capabilities of che-starter and Che IDE. 

## Prerequities 
- Running Che starter
- Running Che server on an OpenShift with properly set up Che server. That means the Che server is running with configuration compatible with the runtime configuration of the running Che starter

## Test execution
Run `mvn clean verify` with arguments listed below to run all tests. 
### Execution arguments
- _preserveWorkspace_ - **optional**, default false; set to true if you want to keep workspace after tests (for further manual work)
- _cheStarterURL_ - **required**; URL of Che starter which is called to handle Che
- _openShiftMasterURL_ - **required**; URL of OpenShift master which is passed to Che starter
- _openShiftToken_ - **required**; token to authenticate to OpenShift