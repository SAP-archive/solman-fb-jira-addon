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
            width : '600px'
            }).on("change", function(e) {
                spGetSolmanJumpUrlSettings();
        });

        function repoFormatResult(result) {
            return '<option value="' + result.id + '" data-aui-trigger aria-controls="show-on-hover" href="show-onhover">' + result.solmanconn.customerDescription + ',' + result.solmanconn.customerGuid + '</option>';
        }

        function repoFormatSelection(result) {
            return '<option value="' + result.id + '">' + result.solmanconn.customerDescription + ',' + result.solmanconn.customerGuid + '</option>';
        }
}

AJS.$(document).ready(function () {
    spGetSolmanCustomerGuids();
    spGetSolmanJumpUrlSettings();
});

function spGetSolmanJumpUrlSettings() {
        AJS.$('#solmanpjumpurlsettings-table-id tbody').empty();

        AJS.$.ajax({
                url: AJS.params.baseURL + '/plugins/servlet/getJumpUrlSettings',
                type: 'post',
                data: 'json',
                success: function (data) {
                    AJS.$.each(data, function (i, s) {
                        AJS.$('#solmanpjumpurlsettings-table-id tbody').append('<tr><td headers="solManCustGuiD">' + s.solManCustGuiD + '</td>' +
                        '<td style="word-break:break-all;" headers="workPackageAppJumpUrl"><a href="' + s.workPackageAppJumpUrl + '">' + s.workPackageAppJumpUrl +'</a></td>' +
                        '<td style="word-break:break-all;" headers="workItemAppJumpUrl"><a href="' + s.workItemAppJumpUrl + '">' + s.workItemAppJumpUrl + '</a></td>' +
                        '<td headers="deleteInfo"><button class="aui-button aui-button-primary" id="' + s.id +
                                                '" onclick="spDeleteSolmanJumpUrlSettings(this);" id="deleteInfo">Delete</button></td></tr>');
                    });
                },
                error: function (err) {
                   AJS.$('#solmanpjumpurlsettings-table-id tbody').replaceWith('<p>No data to display.</p>');
                }
          });
}


function spDeleteSolmanJumpUrlSettings(element) {
           var url = AJS.params.baseURL + '/plugins/servlet/deleteJumpUrlSettings?id='+ element.id;

           AJS.$.ajax({
                 type: "POST",
                 url: url,
                 success: function(data)
                 {
                   spGetSolmanJumpUrlSettings();
                 },
           });
}

function spSaveSolmanJumpUrlSettings() {

          var options = {
              solManCustGuiD : AJS.$("#solman-connections-id").val(),
              workPackageAppJumpUrl : AJS.$("#workPackageAppJumpUrl").val(),
              workItemAppJumpUrl : AJS.$("#workItemAppJumpUrl").val()
          };

          var errorMessage = '';

          if (options.solManCustGuiD == '') {
              errorMessage += 'SolMan Customer GUID must be selected.<br>';
          }

          if (options.workPackageAppJumpUrl == '') {
              errorMessage += 'Work Package App Jump Url should not be empty.<br>';
          }

          if (options.workItemAppJumpUrl == '') {
                errorMessage += 'Work Item App Jump Url should not be empty.<br>';
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
          var url = AJS.params.baseURL + '/plugins/servlet/addJumpUrlSettings?jumpUrlSettings='+ JSON.stringify(options);

          AJS.$.ajax({
              type: "POST",
              url: url,
              success: function(data)
              {
                  spGetSolmanJumpUrlSettings();
              }
          });
}