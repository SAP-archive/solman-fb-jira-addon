function spGetAuthenticationTypes() {
        AJS.$("#authent-types-id").auiSelect2({
        ajax: {
            url: AJS.params.baseURL + '/plugins/servlet/getAuthenticationTypes',
            dataType: 'json',

            results : function(data, page) {
                var myResults = [];
                AJS.$.each(data, function(index, item)
                {
                    myResults.push({
                        'id':item.id,
                        'authtype': item
                    });
                });
                return {
                    results : myResults
                };
            },
            cache:true
        },
        theme:'classic',
        escapeMarkup: function(markup) {return markup;},
        formatResult : repoFormatResult,
        formatSelection : repoFormatSelection,
        dropdownAutoWidth : true
    }).on("change", function(e) {

    });

     function repoFormatResult(result) {
            return '<option value="' + result.authtype.authenticationType + '">' + result.authtype.authenticationType + '</option>';
        }

        function repoFormatSelection(result) {
            return '<option value="' + result.authtype.authenticationType + '">' + result.authtype.authenticationType + '</option>';
        }
}


function spGet() {
        AJS.$('table.aui tbody').empty();


        AJS.$('#table-conn-id').remove();
        AJS.$('#table-conn-id-container').append(
            '<table id="table-conn-id" class="aui aui-table-sortable">' +
            '<thead>' +
                '<tr>' +
                    '<th id="customerDescription">Customer Description</th>' +
                    '<th id="solmanUrl">SolMan URL</th>' +
                    '<th id="userName">User Name</th>' +
                    '<th id="password">Password</th>' +
                    '<th id="customerGuid">Customer SolMan GUID</th>' +
                    '<th id="sapClient">Sap Client</th>' +
                    '<th id="authenticationType">Authentication Type</th>' +
                    '<th id="tokenHcpAccountUrl">Token Hcp Account URL</th>' +
                    '<th></th>' +
                '</tr>' +
            '</thead>' +
            '<tbody>' +
            '</tbody>' +
            '</table>');

        AJS.$.ajax({
            url:  AJS.params.baseURL + '/plugins/servlet/spGet',
            type: 'post',
            data: 'json',
            success: function (data) {
                AJS.$.each(data, function (i, s) {
                    AJS.$('table.aui tbody').append('<tr><td headers="customerDescription">' + s.customerDescription + '</td>' +
                    '<td style="word-break:break-all;" headers="solmanUrl"><a href="' + s.solmanUrl + '">' + s.solmanUrl + '</a></td><td headers="userName">' + s.userName + '</td>' +
                    '<td style="word-break:break-all;" headers="password">' + (s.password != '' ? '*****' : '') + '</td>' +
                    '<td style="word-break:break-all;" headers="customerGuid">' + s.customerGuid + '</td>' +
                    '<td style="word-break:break-all;" headers="sapClient">' + s.sapClient + '</td>' +
                    '<td style="word-break:break-all;" headers="authenticationType">' + s.authenticationType + '</td>' +
                    '<td style="word-break:break-all;" headers="tokenHcpAccountUrl"><a href="' + s.tokenHcpAccountUrl + '">' + s.tokenHcpAccountUrl + '</a></td>' +
                    '<td headers="deleteInfo"><button class="aui-button aui-button-primary" guid="' + s.customerGuid + '" onclick="showRemoveConnectionPopup(this);" id="deleteInfo">Delete</button></td></tr>');
                });
                AJS.tablessortable.setTableSortable(AJS.$("#table-conn-id"));
            },
            error: function (err) {
                AJS.$('table.aui').replaceWith('<p>No data to display.</p>');
            }
        });
}

function deleteInfo(element) {
        var customerGuid = (AJS.$(element).attr("guid"));
        var url = AJS.params.baseURL + '/plugins/servlet/spDelete?customerGuid=' + customerGuid;

        AJS.$.ajax({
            type: "POST",
            url: url,
            success: function(data)
            {
              spGet();
            },
        });
}

function saveSolmanConnection()  {
            var options = {
                solmanUrl : AJS.$("#solmanUrl").val(),
                userName : AJS.$("#userName").val(),
                password : AJS.$("#password").val(),
                customerGuid : AJS.$("#customerGuid").val(),
                customerDescription : AJS.$("#customerDescription").val(),
                sapClient : AJS.$("#sapClient").val(),
                authenticationType : AJS.$('#authent-types-id').val(),
                tokenHcpAccountUrl : AJS.$('#tokenHcpAccountUrl').val()
            }

            var errorMessage = '';

            if (options.solmanUrl == '') {
                errorMessage += 'SolMan Url should not be empty.<br>';
            }

            if (options.customerDescription == '') {
                errorMessage += 'Customer Description should not be empty.<br>';
            }

            if (options.userName == '') {
                errorMessage += 'Username should not be empty.<br>';
            }

            if (options.password == '') {
                errorMessage += 'Password should not be empty.<br>';
            }

            if (options.sapClient == '') {
                errorMessage += 'Sap Client should not be empty.<br>';
            }

            if (options.authenticationType == '') {
                errorMessage += 'Authentication Type should not be empty.<br>';
            }

            if (options.tokenEndpointUrl == '') {
                errorMessage += 'Token endpoint URL should not be empty.<br>';
            }

            if (errorMessage != '') {

                AJS.$('aui-message-bar').empty();

                AJS.messages.error({
                   title: 'Incorrect Solution Manager Connection Information',
                   body: '<p>' + errorMessage + '</p>',
                    duration : 1,
                    fadeout:true
                });
                return;
            }

            var url = AJS.params.baseURL + '/plugins/servlet/spSave?SolmanParams=['+ JSON.stringify(options)+']';

            AJS.$.ajax({
                type: "POST",
                url: url,
                success: function(data)
                {
                    spGet();
                },
            });
}

function showRemoveConnectionPopup(element) {
  var customerGuid = (AJS.$(element).attr("guid"));

  AJS.dialog2("#remove-conn-dialog").show();

  AJS.$("#dialog-submit-button").click(function(e) {
         deleteInfo(element);
         e.preventDefault();
         AJS.dialog2("#remove-conn-dialog").hide();
  });

  // Hides the dialog
  AJS.$("#dialog-close-button").click(function(e) {
        e.preventDefault();
        AJS.dialog2("#remove-conn-dialog").hide();
  });
}

AJS.$(document).ready(function () {
    spGet();
    spGetAuthenticationTypes();
});