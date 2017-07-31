# Solution Manager Focused Build Jira Add-on

## Intro
With Focused Build for SAP Solution Manager SAP delivers a seamless tool-based requirements-to-deploy process within SAP Solution Manager. To satisfy a typical integration scenario during the build phase it provides a generic OData web service API to integration with external software development tools like Jira or MS Team Foundation Server. 

This Jira Add-on provides you an example how to connect Jira to the Solution Manager Focused Build external tool API.

## Pre-requisite
1. You need to have a SAP Solution Manager with Focused Build installed (ST-OST add-on) and configured.
2. You need to have your own Jira Instance.

![alt text](https://github.com/SAP/solman-fb-jira-addon/blob/master/doc/Integration_Overview.png "Integration Overview")

## What it can do 

The integration allows development teams to use Jira during the build phase. All other parts of the requirement to deploy process remain in SAP Solution Manager. Starting point for the interaction with Jira is once the architects finished the scoping of the Work Packages. With the status change *handover to development* the external tool API in Focused Build is triggered and creates for Work Packages and Work Items corresponding issues (Epic and User Story) on Jira side.    

For the User Story Jira will be the leading systems, which means the status is changed on Jira side and synchronized back to Solution Manager to update the status of the Work Item. For the Work Packages Solution Manager remains the leading system. All status changes of the Work Packages are done on Solution Manager side (implicitly via the status changes of the corresponding Work Items) and the status change is synchronized to Jira.    

![alt text](https://github.com/SAP/solman-fb-jira-addon/blob/master/doc/Issue_Relation.png "Issue Relation")

During the initial creation of the Epics and User Stories also the assigned documents from Solution Documentation are transferred to Jira and attached to issue.



## What it cannot do
- As the external tool API of Focused Build does not allow the creation of objects on Solution Manager side this Jira add-on only creates issues on Jira side. You cannot use it create objects (business requirement, IT requirement, change,...) in Solution Manager out of Jira issues.
- Once the issues are created on Jira side only status updates are exchanged between Solution Manager and Jira. There is not delta update on both sides for other information (e.g. description change, effort change,...) 

## How to start
- get the code from this repository
- build the Jira add-on
- deploy the Jira add-on on a Jira instance in a test environment
- perform required [configuration on Jira side](https://github.com/SAP/solman-fb-jira-addon/blob/master/doc/JIRA_CONFIGURATION.md)
- perform the [Jira Add-on configuration](https://github.com/SAP/solman-fb-jira-addon/blob/master/doc/ADDON_CONFIGURATION.md)
- perform the required configuration of the [Solution Manager external tool API](https://github.com/SAP/solman-fb-jira-addon/blob/master/doc/External_Tool_Integration_API_V2.0.pdf)
