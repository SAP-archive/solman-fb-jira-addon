function spGetSolmanCustomerGuids() {
        AJS.$("#solman-connections-id").auiSelect2({
        ajax: {
            url: AJS.params.baseURL + '/plugins/servlet/spGet',
            dataType: 'json',

        results : function(data, page) {
            var myResults = [];
            AJS.$.each(data, function(index, item)
            {
                myResults.push({
                    'id':item.customerGuid,
                    'solmanconn': item
                });
            });
            return {
              results : myResults
            };
        },
            cache:true
            },
            escapeMarkup: function(markup) {return markup;},
            formatResult : repoFormatResult,
            formatSelection : repoFormatSelection,
            width : '100%'
            }).on("change", function(e) {
                spGetSolmanIssueTypeMappings(e.val);
        });

        function repoFormatResult(result) {
            return '<option value="' + result.id + '" data-aui-trigger aria-controls="show-on-hover" href="show-onhover">' + result.solmanconn.customerDescription + ',' + result.solmanconn.customerGuid + '</option>';
        }

        function repoFormatSelection(result) {
            return '<option value="' + result.id + '">' + result.solmanconn.customerDescription + ',' + result.solmanconn.customerGuid + '</option>';
        }
}


var issueTypes = [];
issueTypes.push({
    'id': 10000,
    'text': "Epic"
});
issueTypes.push({
    'id': 10001,
    'text': "User Story"
});
function getIssueTypeName(id){
    for (var i = 0; i < issueTypes.length; i++){
        if (issueTypes[i].id == id){
            return issueTypes[i].text;
        }
    }
}

function spGetJiraIssueTypes() {
    AJS.$("#jira-issue-type").auiSelect2({
            data: issueTypes
        });
}

function spGetSolmanIssueTypeMappings(customerGuid) {
     AJS.$('#issuetypemapping-table-id tbody').empty();

     AJS.$.ajax({
                url: AJS.params.baseURL + '/plugins/servlet/getIssueTypeMapping?solManCustGuiD=' + customerGuid,
                type: 'post',
                data: 'json',
                success: function (data) {
                    AJS.$.each(data, function (i, s) {
                        AJS.$('#issuetypemapping-table-id tbody').append('<tr><td headers="solmanProcessType">' + s.solmanProcessType + '</td>' +
                        '<td style="word-break:break-all;" headers="jiraIssueType">' + getIssueTypeName( s.jiraIssueType ) + '</td>' +
                        '<td headers="deleteInfo"><button class="aui-button aui-button-primary" id="' + s.solmanProcessType + '" onclick="spDeleteSolmanIssueTypeMapping(this);" id="deleteInfo">Delete</button></td></tr>');
                    });
                },
                error: function (err) {
                   AJS.$('#solman-issuetypemapping-id tbody').replaceWith('<p>No data to display.</p>');
                }
          });
}

function spSaveSolmanIssueTypeMapping() {

          var options = {
              solManCustGuiD : AJS.$("#solman-connections-id").val(),
              solmanProcessType : AJS.$("#solmanProcessType").val(),
              jiraIssueType : AJS.$("#jira-issue-type").val()
          };

          var errorMessage = '';

          if (options.solManCustGuiD == '') {
              errorMessage += 'SolMan Customer GUID must be selected.<br>';
          }

          if (options.solmanProcessType == '') {
              errorMessage += 'SolMan Process type should not be empty.<br>';
          }

          if (options.jiraIssueType == '') {
                errorMessage += 'Jira Issue Type should not be empty.<br>';
          }

          if (errorMessage != '') {
               AJS.$('aui-message-bar').append('');
               AJS.messages.error({
                  title: 'Incorrect Solution Manager Issue Type Mapping Information',
                  body: '<p>' + errorMessage + '</p>',
                  duration : 1,
                  fadeout:true
               });
               return;
          }
          var url = AJS.params.baseURL + '/plugins/servlet/addIssueTypeMapping?'+
              'solManCustGuiD=' + options.solManCustGuiD +
              '&solManProcessType=' + options.solmanProcessType +
              '&jiraIssueType=' + options.jiraIssueType;

          AJS.$.ajax({
              type: "POST",
              url: url,
              success: function(data)
              {
                  spGetSolmanIssueTypeMappings(options.solManCustGuiD);
              }
          });
}

function spDeleteSolmanIssueTypeMapping(element) {
           var options = {
                      solManCustGuiD : AJS.$("#solman-connections-id").val(),
                      id : AJS.$(element).attr("id")
           };
           var url = AJS.params.baseURL + '/plugins/servlet/deleteIssueTypeMapping?'+
               'solManCustGuiD=' + options.solManCustGuiD +
               '&solManProcessType=' + options.id;

           AJS.$.ajax({
                 type: "POST",
                 url: url,
                 success: function(data)
                 {
                   spGetSolmanIssueTypeMappings(options.solManCustGuiD);
                 }
           });
}

AJS.$(document).ready(function () {
    spGetSolmanCustomerGuids();
    spGetJiraIssueTypes();
});