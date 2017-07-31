# Configuration of the Jira Add-on
The configuration UIs of the Add-on you find in the Jira *Administration* area in the *add-on* section. This configuration is required to establish the connectivity to the Solution Manager system and to customize the issue and field mapping. 


## SolMan Connections 
In the *SolMan Connections* area you specify the connectivity parameters 

![alt text](https://github.com/SAP/solman-fb-jira-addon/blob/master/doc/SolMan_Connections.png "SolMan Connections")

**Customer Description:**
This is just a description text of the connection

**SolMan URL:** 
Here you specify the base URL of your Solution Manager system. E.g. https://\<SolManHost\>:\<port\>

**User Name:**
User name of the Solution Manager user.

**Password:** 
Password of the Solution Manager user.

**Customer SolMan GUID:** 
This is the GUID of the Solution Manager which is send with all requests from Solution. This is used to identify the correct connection when communicating back to Solution Manager. This allows you to connect more than one Solution Manager system.
To get this GUID logon to your Solution Manager system, start transaction SE16 and display all values in table /SALM/SYSGUID. In case the table is not filled you can execute the method GET_SYSTEM_GUID of Class /SALM/CL_EXT_INTEG_API to generate the GUID.

**SAP Client:**
The SAP Client in your Solution Manager system you want to connect to.

**Authentication Type:**
You have to use *basic*.

**Token Endpoint URL:**
Leave this field empty 


## SolMan Proxy Settings
In case a proxy is required for the connectivity from Jira to Solution Manager you can maintain it here.

## SolMan Issue Mapping
In the *SolMan Issue Mapping* section you have to map the project, the issue types and the status (for incoming status updates).
![alt text](https://github.com/SAP/solman-fb-jira-addon/blob/master/doc/SolMan_Issue_Mapping.png "SolMan Issue Mapping")

**Project Mapping:** Here you map the PPM project ID from SolMan side to the Jir project key. This is always a 1:1 mapping.

**Issue Type Mapping:** Here you map the SolMan transaction type to the Jira Issue Type.

**Issue Inbound Status Mapping:** Here you map the status of a SolMan transaction type to a specific Jira transition. 



## SolMan Field Mapping
In the *SolMan Filed Mapping* section you can map fields from SolMan side to fields on Jira side. There are two different types of fields. The simple fields for which a 1:1 mapping can be done and the nested fields where additional information is required to perform the mapping. You can only map those fields which are available in the JSON structure send by Solution Manager.

![alt text](https://github.com/SAP/solman-fb-jira-addon/blob/master/doc/SolMan_Field_Mapping.png "SolMan Field Mapping")

**Simple Field Mapping:** Here you a map simple fields of a SolMan transaction type to a field on Jira side.

**Partner Field Mapping:** As there are several partner functions per transaction type in SolMan you have to specify the field of a partner function per transaction type and map it to a Jira field.

**Text Field Mapping:** As there are several text types per transaction type in SolMan you have to specify the text type per transaction type and map it to a Jira field.

**Appointment Field Mapping:** As there are several appointments types per transaction type in SolMan you have to specify the appointment type per transaction type and map it to a Jira field. 


## SolMan Jump-in URLs
Here you specify the jump-in links to the Solution Manager Work Package and Work Item UI5 Apps.

**Work Package UI5 App Link:**
https://\<SolManHost\>:\<port\>/sap/bc/bsp/salm/ost_wp/index.html?WP_GUID=

**Work Item UI5 App Link:**
https://\<SolManHost\>:\<port\>/sap/bc/bsp/salm/ost_wi/index.html?WS_GUID=
