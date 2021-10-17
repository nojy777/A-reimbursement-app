document.getElementById('create-btn').addEventListener("click", attemptCreate);

function attemptCreate() {
    const errorDiv = document.getElementById("error-div");
    const successDiv = document.getElementById("success-div");
    errorDiv.hidden = true;
    successDiv.hidden = true;

    // get token from local storage
    var authToken = localStorage.getItem("token");

    // get input values from input fields
    const title = document.getElementById("title").value;
    const description = document.getElementById("description").value;
    const amount = document.getElementById("amount").value;

    if(title == "" || description == "" || amount == "") {
        errorDiv.hidden = false;
        errorDiv.innerText = "All the fields are required";
        return;
    }

    console.log(`title: ${title}, description: ${description}, amount: ${amount}`);

    const xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:8080/michelle-ers/reimbursement/create");

    // define onreadystatechange callback for the xhr object (check for readystate 4)
    xhr.onreadystatechange = function(){
        if(xhr.readyState===4){
            const respObj = JSON.parse(xhr.response);
            if(respObj.status) {
                 successDiv.hidden = false;
                 successDiv.innerText = respObj.message;
                 document.getElementById('create-request-form').reset();
            } else {
                errorDiv.hidden = false;
                errorDiv.innerText = respObj.message;
            }
        }
    }

    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhr.setRequestHeader("Authorization", authToken);

    // send request, with the username and password in the request body
    const requestBody = `title=${title}&description=${description}&amount=${amount}`;
    xhr.send(requestBody);

}