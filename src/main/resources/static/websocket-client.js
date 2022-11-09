var uri = "ws://localhost:8080/ws/websockets";
var websocket = null;

function openSession() {
    checkAndCloseSessionIfAlreadyOpen();

    websocket = new WebSocket(uri);
    websocket.addEventListener('open', function (e) {
        var clientConnectedMessage = "Client connected to: " + uri;
        websocket.send(clientConnectedMessage);
        log(clientConnectedMessage);
    });
    websocket.addEventListener('message', function (e) {
        var msg = e.data;
        var clientMessage = msg + " from client";
        websocket.send(clientMessage);
        log(msg);
        log(clientMessage);
    });
    websocket.addEventListener('close', function (e) {
        var clientClosedMessage = "Client disconnected from: " + uri;
        log(clientClosedMessage);
    });
    websocket.addEventListener('error', function (e) {
        var clientClosedMessage = "Something went wrong with: " + uri;
        log(clientClosedMessage);
    });
}

// Preventing client from creating multiple sessions.
function checkAndCloseSessionIfAlreadyOpen() {
    if (websocket!=null && websocket.readyState!=3) {
        log("Client session is already open; closing session now..");
        closeSession();
    }
}

function statusSession() {
    if (websocket==null) {
        log("Current websocket status is: -1");
    } else {
        log("Current websocket status is: " + websocket.readyState)
    }
}

function closeSession() {
    if (websocket!=null) {
        websocket.close();
    } else {
        log("Session already closed!");
    }
    return false;
}

function clearData() {
    document.getElementById('websocketMessages').innerHTML = "";
}

function log(msg) {
    var websocketMessagesDiv = document.getElementById('websocketMessages');
    var element = document.createElement('div');
    var text = document.createTextNode(msg);
    element.appendChild(text);
    websocketMessagesDiv.append(element);
}