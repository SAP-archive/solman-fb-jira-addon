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
            theme:'classic',
            escapeMarkup: function(markup) {return markup;},
            formatResult : repoFormatResult,
            formatSelection : repoFormatSelection,
            dropdownAutoWidth : true,
            width : '600px'
            }).on("change", function(e) {
                spGetJiraQueueProcessing(e.val);
                getLastExecutionTime(e.val);
        });

        function repoFormatResult(result) {
            return '<option value="' + result.id + '">' + result.solmanconn.customerDescription + ',' + result.solmanconn.customerGuid + '</option>';
        }

        function repoFormatSelection(result) {
            return '<option value="' + result.id + '">' + result.solmanconn.customerDescription + ',' + result.solmanconn.customerGuid + '</option>';
        }
}

var sendTypes = new Array();
var processingStatuses = new Object();

function spGetSendTypes() {
           AJS.$.ajax({
                url: AJS.params.baseURL + '/plugins/servlet/getSendTypes',
                type: 'post',
                data: 'json',
                success: function (data) {
                    AJS.$.each(data, function (i, s) {
                       sendTypes[s.id] = s.name;
                    });
                },
                error: function (err) {

                }
          });
}

function spGetProcessingStatuses() {
 AJS.$.ajax({
                url: AJS.params.baseURL + '/plugins/servlet/getProcessingStatuses',
                type: 'post',
                data: 'json',
                success: function (data) {
                    AJS.$.each(data, function (i, s) {
                       processingStatuses[s.id] = s.name;
                    });
                },
                error: function (err) {

                }
          });
}

function getSendType(sendTypeId) {
    return sendTypes[sendTypeId];
}

function getProcessingStatus(processingStatusId) {
    return processingStatuses[processingStatusId];
}

function spGetJiraQueueProcessing(solManCustGuid, page, recordsPerPage, orderBy, direction) {

  if (orderBy == null && AJS.$('#sort-span-id').length != 0) {
    orderBy = AJS.$("#sort-span-id").parent().attr('id');
    var _class = AJS.$('#sort-span-id').attr('class');
    if (_class.indexOf('down') >= 0) {
        direction = "desc";
    } else {
        direction = "asc";
    }
  }

  var options = {
                        solManCustGuiD : solManCustGuid,
                        page : page,
                        recordsPerPage : recordsPerPage,
                        issueKey : AJS.$("input[name='issueKey']").val(),
                        unsynchronizedIssueCreationDate : AJS.$("input[name='creationDate']").val(),
                        orderBy : orderBy,
                        direction : direction
    }

    AJS.$('#unsynchronized-issues-table-id tbody').empty();

    AJS.$('#unsynchronized-issues-id').remove();
    AJS.$('#unsynchronized-issues-navigation-id').empty();
    AJS.$("#pages > ul").empty();

    AJS.$.ajax({
                url: AJS.params.baseURL + '/plugins/servlet/getUnsychronizedIssues?unsynchronizedIssueRequestParameters='+ JSON.stringify(options),
                type: 'post',
                data: 'json',
                success: function (data, status, xhr) {

                    var maxPageSize = xhr.getResponseHeader('maxPageSize');
                    var issueKey = xhr.getResponseHeader('issueKey');
                    if (issueKey != null) {
                        AJS.$("input[name='issueKey']").val(issueKey);
                    }
                    var currentPage = xhr.getResponseHeader('currentPage');
                    var nextPage = parseInt(currentPage) + 1;
                    var prevPage = parseInt(currentPage) - 1;
                    var recordsPerPage = parseInt(xhr.getResponseHeader('recordsPerPage'));
                    var rowsPerPage = parseInt(AJS.$('#rows-per-page').find('.aui-dropdown2-radio.checked').text());

                    AJS.$.each(data, function (i, s) {
                        AJS.$('#unsynchronized-issues-table-id tbody').append('<tr id="' + s.id + '"><td headers="issueKey"><a href="' + AJS.params.baseURL + '/browse/' + s.issueKey + '">' + s.issueKey + '</a></td>' +
                        '<td style="word-break:break-all;" headers="solmanGuid">' + s.solmanGuid + '</td>' +
                        '<td style="word-break:break-all;" headers="sendType">' + getSendType(s.sendType) + '</td>' +
                        '<td style="word-break:break-all;" headers="sendDetails">' + s.newValue + '</td>' +
                        '<td style="word-break:break-all;" headers="creationDate">' + formatDate(s.creationDate) + '</td>' +
                        '<td style="word-break:break-all;" headers="processingStatus">' + getProcessingStatus(s.processingStatus) + '</td>' +
                        '<td style="word-break:break-all;" headers="lastProcessingDate">' + formatDate(s.lastProcessingDate) + '</td>' +
                        '<td style="word-break:break-all;" headers="successful">' + (s.successful == true ? '<span class="aui-icon aui-icon-small aui-iconfont-success"></span>' : '') + '</td>' +
                        '<td style="word-break:break-all;" headers="successfulDate">' + formatDate(s.synchronizationDate) + '</td>' +
                        '<td style="word-break:break-all;" headers="lastProcessor">' + s.lastProcessor + '</td></tr>');
                    });
                    AJS.$("#unsynchronized-issues-navigation-id").append('<li class="aui-nav-first"><a onclick=\'spGetJiraQueueProcessing(\"' + solManCustGuid + '", 1, ' + rowsPerPage + ')\' href="#">First</a></li>' +
                               ((prevPage != 0 && maxPageSize != 0) ? '<li class="aui-nav-previous"><a onclick=\'spGetJiraQueueProcessing(\"' + solManCustGuid + '",' + prevPage + ', ' + rowsPerPage + ')\' href="#">Prev</a></li>' : '<li class="aui-nav-previous"><a disabled="disabled" href="#">Prev</a></li>') +
                               '<li class="aui-nav-selected" id="current-page-id"><a href="#">' + currentPage + '</a></li>' +
                               ((nextPage != parseInt(maxPageSize) + 1 && maxPageSize != 0) ? '<li class="aui-nav-next"><a onclick=\'spGetJiraQueueProcessing(\"' + solManCustGuid + '",' + nextPage + ', ' + rowsPerPage + ')\' href="#">Next</a></li>' : '<li class="aui-nav-next"><a disabled="disabled" href="#">Next</a></li>') +
                               (maxPageSize != 0 ? '<li class="aui-nav-last"><a onclick=\'spGetJiraQueueProcessing(\"' + solManCustGuid + '",' + maxPageSize + ', ' + rowsPerPage + ')\' href="#">Last</a></li>' : '<li class="aui-nav-last"><a href="#">Last</a></li>'));

                    AJS.$("#unsynchronized-issues-navigation-id").append('<a href="#pages" style="color:black" aria-owns="pages" aria-haspopup="true" class="aui-button aui-style-default aui-dropdown2-trigger">Go To Page</a>' +
                     '<div id="pages" class="aui-style-default aui-dropdown2">' +
                            '<ul class="aui-list-truncate">' +
                            '</ul>' +
                       '</div>');

                   AJS.$("#unsynchronized-issues-navigation-id").append('<a href="#rows-per-page" aria-owns="rows-per-page" aria-haspopup="true" style="color:black" class="aui-button aui-style-default aui-dropdown2-trigger">Rows per page</a>' +
                                                                        '<div id="rows-per-page" class="aui-dropdown2 aui-style-default"> ' +
                                                                                 '<ul>' +
                                                                                     '<li><a onclick=\'spGetJiraQueueProcessing(\"' + solManCustGuid + '",' + null + ', 10 )\' href="#" class="aui-dropdown2-radio ' + (recordsPerPage == 10 ? " checked" : "") + '">10</a></li>' +
                                                                                     '<li><a onclick=\'spGetJiraQueueProcessing(\"' + solManCustGuid + '",' + null + ', 20 )\' href="#" class="aui-dropdown2-radio ' + (recordsPerPage == 20 ? " checked" : "") + '">20</a></li>' +
                                                                                     '<li><a onclick=\'spGetJiraQueueProcessing(\"' + solManCustGuid + '",' + null + ', 50 )\' href="#" class="aui-dropdown2-radio ' + (recordsPerPage == 50 ? " checked" : "") + '">50</a></li>' +
                                                                                 '</ul>' +
                                                                         '</div>');
                    AJS.$.each(new Array(parseInt(maxPageSize)), function(index, value ) {
                                AJS.$("#pages > ul").append('<li><a onclick=\'spGetJiraQueueProcessing(\"' + solManCustGuid + '",' + (index + 1) + ', ' + AJS.$('#rows-per-page').find('.aui-dropdown2-radio.checked').text() + ')\' href="#">' + (index + 1) + '</a></li>');
                    });

                    AJS.$("tr").click(function(){
                            AJS.$("tr").children().removeClass("highlighted");
                            AJS.$(this).children().toggleClass("highlighted");
                    });
                },
                error: function (err) {
                   AJS.$('#unsynchronized-issues-table-id tbody').replaceWith('<p>No data to display.</p>');
                }
          });
}

function getLastExecutionTime(solManCustGuiD) {
      if (solManCustGuiD == "") {
          return;
      }
      AJS.$.ajax({
                url: AJS.params.baseURL + '/plugins/servlet/getSolmanPluginJobRunnerInfo?solManCustGuiD='+ solManCustGuiD,
                type: 'post',
                data: 'json',
                success: function (data, status, xhr) {
                        var lastExecutionTimestamp = parseInt(xhr.getResponseHeader('lastExecutionTimestamp'));
                        AJS.$('#lastExecutionTimestampId').empty();
                        AJS.$('#lastExecutionTimestampId').append('Last Runtime of Queue Processor: ' + formatDate(lastExecutionTimestamp));
                }
      });
}

function sortUnsynchronizedIssues() {
            AJS.$('#sort-header-row-id > th').click(function() {
            var id = AJS.$(this).attr('id');
            var solManCustGuiD = AJS.$("#solman-connections-id").val();
            if (solManCustGuiD == null) {
                return;
            }
            var rowsPerPage = AJS.$('#rows-per-page').find('.aui-dropdown2-radio.checked').text();

            if (AJS.$(this).children().length == 1) {
               var _class = AJS.$(this).children().attr('class');
               if (_class.indexOf('down') >= 0) {
                    AJS.$(this).children().remove();
                    AJS.$(this).append('<span id="sort-span-id" class="aui-icon aui-icon-small aui-iconfont-up"></span>');
                    spGetJiraQueueProcessing(solManCustGuiD, AJS.$("#current-page-id > a").text(), rowsPerPage, id, "asc");
               } else {
                    AJS.$(this).children().remove();
                    AJS.$(this).append('<span id="sort-span-id" class="aui-icon aui-icon-small aui-iconfont-down"></span>');
                    spGetJiraQueueProcessing(solManCustGuiD, AJS.$("#current-page-id > a").text(), rowsPerPage, id, "desc");
               }
            } else {
                    AJS.$('#sort-header-row-id > th > span').remove();
                    AJS.$(this).append('<span id="sort-span-id" class="aui-icon aui-icon-small aui-iconfont-down"></span>');
                    spGetJiraQueueProcessing(solManCustGuiD, AJS.$("#current-page-id > a").text(), rowsPerPage, id, "desc");
            }
    });
}

function formatDate(element) {
    if(element == null) {
        return "-"
    }
    return new Date(element).toString();
}

function retryIssueSynchronization() {
   var id = AJS.$("td[class='highlighted']").parent().attr("id");
   if (id == null) {
              AJS.$('aui-message-bar').empty();
              AJS.messages.error({
                          title: 'Not selected unsynchronized issue',
                          body: '<p> Issue should be selected </p>',
                          duration : 1,
                          fadeout:true
              });
   return;
}
   AJS.$.ajax({
            url: AJS.params.baseURL + '/plugins/servlet/retryIssueStatusSyncronization?id=' + id,
            type: 'post',
            data: 'json',
            success: function (data) {
                      spRefreshListIssues();
            },
            error: function (err, exception) {
                var response = AJS.$.parseJSON(err.responseText);
                if ((response.code >= 400 && response.code <= 428) || response.code == -1)  {
                    AJS.$('#header-error-message-id').text('Communication Error');
                    if (AJS.$('#div-aui-message-id').hasClass('error')) {
                      AJS.$('#div-aui-message-id').switchClass('error', 'warning', 0);
                    } else {
                      AJS.$('#div-aui-message-id').addClass('aui-message warning');
                    }
                    AJS.$('#div-aui-message-id > span').addClass('aui-icon icon-warning');
                    AJS.$('#error-message-id').text(response.detail);
                    AJS.dialog2('#unsynchronized-issue-dialog').show();
                } else if (response.code >= 500 && response.code <= 505) {
                    AJS.$('#header-error-message-id').text('Application Error');
                    if (AJS.$('#div-aui-message-id').hasClass('warning')) {
                      AJS.$('#div-aui-message-id').switchClass('warning', 'error', 0);
                    } else {
                      AJS.$('#div-aui-message-id').addClass('aui-message error');
                    }
                     AJS.$('#div-aui-message-id > span').addClass('aui-icon icon-error');
                    AJS.$('#error-message-id').text(response.detail);
                    AJS.dialog2('#unsynchronized-issue-dialog').show();
                }
            }
   });
}

function setToSuccessful() {
    var id = AJS.$("td[class='highlighted']").parent().attr("id");
    if (id == null) {
           AJS.$('aui-message-bar').empty();
           AJS.messages.error({
                        title: 'Not selected unsynchronized issue',
                        body: '<p> Issue should be selected </p>',
                        duration : 1,
                        fadeout:true
                    });
             return;
    }

    AJS.$.ajax({
        url: AJS.params.baseURL + '/plugins/servlet/setIssueToSynchronized?id=' + id,
        type: 'post',
        data: 'json',
        success: function (data) {
                  spRefreshListIssues();
        },
        error: function (err) {

        }
    });
}

function deleteSuccessfulEntries() {
    var solManCustGuiD = AJS.$("#solman-connections-id").val();
    AJS.$.ajax({
            url: AJS.params.baseURL + '/plugins/servlet/deleteSynchronizedIssues?solManCustGuiD=' + solManCustGuiD,
            type: 'post',
            data: 'json',
            success: function (data) {
                      spRefreshListIssues();
            },
            error: function (err) {

            }
    });
}

function spRefreshListIssues() {
    AJS.$('#sort-span-id').remove();
    var solManCustGuiD = AJS.$("#solman-connections-id").val();
    getLastExecutionTime(solManCustGuiD);
    var rowsPerPage = AJS.$('#rows-per-page').find('.aui-dropdown2-radio.checked').text();
    spGetJiraQueueProcessing(solManCustGuiD, AJS.$("#current-page-id > a").text(), rowsPerPage);
}

AJS.$(document).ready(function () {
    AJS.$("#sendTypeId").auiSelect2();
    spGetSolmanCustomerGuids();
    spGetSendTypes();
    spGetProcessingStatuses();
    sortUnsynchronizedIssues();

    AJS.$("input[name='issueKey']").keyup(function() {
          spGetJiraQueueProcessing(AJS.$("#solman-connections-id").val());
    });
    AJS.$('#creation-date').datePicker({'overrideBrowserDefault': true});

    AJS.$("#creation-date").change(function() {
       spGetJiraQueueProcessing(AJS.$("#solman-connections-id").val());
    });

});