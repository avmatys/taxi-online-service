/**
 * Represent ajax request function
 * @returns {{ajaxRequest: ajaxRequest}}
 * @constructor
 */
var AjaxWrapper = function(){
    /**
     * Function is used for sending ajax requests to REST web service
     * @param data - username login and password
     * @param dataType - type of data (JSON or some other)
     * @param contentType
     * @param timeout
     * @param type - GET, POST, PUT and etc.
     * @param url - url of the server
     * @param onDone - function for handling successful authorization
     * @param onFail - function for handling authorization failure
     */

    function ajaxRequest(data, dataType, contentType, timeout, type, beforeSend, url, onDone, onFail) {
        $.ajax({
            data: data,
            dataType: dataType,
            contentType: contentType,
            timeout: timeout,
            type: type,
            beforeSend: beforeSend,
            url: url

        }).done(function (data, textStatus, jqXHR) {
            onDone(data);
        }).fail(function (jqXHR, textStatus, errorThrown) {
            onFail(jqXHR);
        });
    }
    return {
        ajaxRequest: ajaxRequest
    }
};