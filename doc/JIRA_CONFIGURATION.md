# Jira Configuration
There is configuration required on Jira side in order to get the Jira Add-on working.

## Communication User
The Jira user which is used for the communication from Solution Manager to Jira needs to fulfill some requirements:
1. The user needs to be in the user group **SolManIntegrationAdmin**. As this user group group does not exist by default you need to create it first (take care of the correct upper/lowercase spelling) and then assign the communication user to it.
2. Due to field length restriction on ABAP side the communication user must not be longer than 12 characters. 

## Required Issue Fields
The Jira Add-on requires some fields to exist in an issue. The following list shows all required fields. You have to take care that the field name is exactly as written in the list (upper/lowercase) and also the correct field types are used. The *Solman\** field types are only available once the Add-on is deployed. 

![alt text](https://github.com/SAP/solman-fb-jira-addon/blob/master/doc/Required_Issue_Fields.png "Required Issue Fields")


## Workflow Transition Validator
For the status update back to Solution Manager a transition validator is used. You have to configure this SolMan validator (available once the Add-on is deployed) into the issue workflow which is used for the issue type corresponding to the Solution Manager Work Item. For every transition which maps to a status change of the Work Item on Solution Manager side you need to add this validator.

As the validator just sends the transition name to Solution Manager you need to adapte the correspondig inbound status mapping in the Focsued Build customizing. See the [External Tool Integration API in Focused Build](https://github.com/SAP/solman-fb-jira-addon/blob/master/doc/External_Tool_Integration_API_V2.0.pdf) document for details.     

