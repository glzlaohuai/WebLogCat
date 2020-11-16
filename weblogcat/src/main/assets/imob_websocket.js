String.prototype.format = function () {
    var formatted = this;
    for (var arg in arguments) {
        formatted = formatted.replace("{" + arg + "}", arguments[arg]);
    }
    return formatted;
};

var autoScroll = false;
var log_div;
var autoScrollCheckBox;
var tagInput;
var filteredTag = "";
var connectionStatus;
var logLevelElement;
var currentLogLevel = 2;

window.onload = function () {//do something
    var ws = new WebSocket("ws://{0}:8089/log".format(document.location.hostname));
    log_div = document.getElementById("log_content");
    device_element = document.getElementById("device_name");
    autoScrollCheckBox = document.getElementById("log-autoscroll");
    tagInput = document.getElementById("log-tag");
    connectionStatus = document.getElementById("connect_status");
    logLevelElement = document.getElementById("log_level_drop");

    ws.onopen = function (evt) {
        console.log("Connection open ...");
        onConnected();
    };

    ws.onmessage = function (evt) {
        console.log("Received Message: " + evt.data);
        handleData(evt.data);
    };

    ws.onclose = function (evt) {
        console.log("Connection closed.");
        onDisconnected();

    };

    tagInput.addEventListener("input", tagUpdated, false);
}

function onConnected() {
    connectionStatus.innerHTML = "Connected";
    connectionStatus.style.color = "forestgreen"
}

function onDisconnected() {
    connectionStatus.innerHTML = "Disconnected";
    connectionStatus.style.color = "maroon";
}

function changeLogLevel(logLevel) {
    currentLogLevel = logLevel;
    var showedLevel;
    switch (logLevel.toLowerCase()) {
        case "v":
            showedLevel = "Verbose";
            break;
        case "d":
            showedLevel = "Debug";
            break;
        case "i":
            showedLevel = "Info";
            break;
        case "w":
            showedLevel = "Warn";
            break;
        case "e":
            showedLevel = "Error";
            break;
    }
    currentLogLevel = logLevelToInt(currentLogLevel);
    logLevelElement.innerHTML = showedLevel;
    filterLogsWithLogLevel();
}

function filterLogsWithLogLevel() {
    updateDisplayStyleWithUpdatedFilterTagAndLogLevel();
}


function tagUpdated() {
    filteredTag = tagInput.value;
    filteredTag = filteredTag.trim().toLowerCase();
    updateDisplayStyleWithUpdatedFilterTagAndLogLevel();
}


function updateDisplayStyleWithUpdatedFilterTagAndLogLevel() {
    var nodes = log_div.childNodes;
    nodes.forEach(element => {
        //display
        if (element.innerHTML && element.innerHTML.toLowerCase().includes(filteredTag) && isThisLogLevelShouldShow(element.className)) {
            element.style.display = "";
        } else {
            element.style.display = "none";
        }
    });
}


function updateAutoScroll() {
    autoScroll = autoScrollCheckBox.checked;
}


function handleData(jsonString) {
    var jsonObject = JSON.parse(jsonString);
    var type = jsonObject.type;
    if (type == "device") {
        device_element.innerHTML = jsonObject.data.device;
    } else if (type == "log") {
        var tag = jsonObject.data.tag;
        var msg = jsonObject.data.msg;
        var level = jsonObject.data.level;
        var warns = jsonObject.data.warns;
        var time = jsonObject.time;
        appendLog(tag, arrayToMsg(msg), level, arrayToMsg(warns), time);
    }
}

function arrayToMsg(array) {
    if (array && array.length > 0) {
        var result = "";
        for (let index = 0; index < array.length; index++) {
            result += array[index];
            if (index != array.length - 1) {
                result += "<br>";
            }
        }
        return result;
    }
}


function decideDisplayAttribute(tag, msg, warns, level) {
    if (isThisLogLevelShouldShow(level) && (tag && tag.toLowerCase().includes(filteredTag) || msg && msg.toLowerCase().includes(filteredTag) || warns && warns.toLowerCase().includes(filteredTag))) {
        return "";
    } else {
        return "display: none;"
    }
}

function logLevelToInt(level) {
    if (!level) {
        return 0;
    }

    var result;
    switch (level.toLowerCase()) {
        case "v":
            result = 2;
            break;

        case "d":
            result = 3;
            break;

        case "i":
            result = 4;
            break;

        case "w":
            result = 5;
            break;

        case "e":
            result = 6;
            break;
    }

    return result;
}

function isThisLogLevelShouldShow(thisLogLevel) {
    thisLogLevel = parseInt(thisLogLevel);
    return thisLogLevel >= currentLogLevel;
}


function appendLog(tag, msg, level, warns, time) {
    var original = log_div.innerHTML;
    var stringLevel;

    var color;
    switch (level) {
        case 2:
            color = "#BBBBBB";
            stringLevel = "V";
            break;
        case 3:
            color = "#0070BB";
            stringLevel = "D";
            break;
        case 4:
            color = "#48BB31";
            stringLevel = "I";
            break;
        case 5:
            color = "#BBBB23";
            stringLevel = "W";
            break;
        case 6:
            color = "#FF0006";
            stringLevel = "E";
            break;
    }
    time = moment(time).format();
    original = original + '<p style="color:{0};{1}" class="{2}">'.format(color, decideDisplayAttribute(tag, msg, warns, level), level) + "< " + time + " > - < " + stringLevel + " > [ " + tag + " ]: " + (msg ? msg : "") + (warns ? "<br>" + warns : "") + "</p>";
    log_div.innerHTML = original;
    if (autoScroll) {
        log_div.scrollIntoView({ block: "end" });
    }
}



