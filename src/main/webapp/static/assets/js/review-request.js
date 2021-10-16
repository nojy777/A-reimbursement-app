const urlParams = new URLSearchParams(window.location.search);

const requestId = urlParams.get('requestId');
const status = urlParams.get('status');

// get token from local storage
var authToken = localStorage.getItem("token");


const xhr = new XMLHttpRequest();
xhr.open("POST", "http://localhost:8080/michelle-ers/manager/review-request");

xhr.onreadystatechange = function(){
    if(xhr.readyState===4){
        const respObj = JSON.parse(xhr.response);
        console.log(respObj)
        if(respObj.status) {
            window.location.href="http://localhost:8080/michelle-ers/static/manager/pending-request.html";
        } else {
            window.location.href="http://localhost:8080/michelle-ers/static/manager/pending-request.html";
        }
    }
}

xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
xhr.setRequestHeader("Authorization", authToken);
const requestBody = `requestId=${requestId}&status=${status}`;
xhr.send(requestBody);

