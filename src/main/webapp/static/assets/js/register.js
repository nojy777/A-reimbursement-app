document.getElementById('register-btn').addEventListener("click", attemptRegister);

function attemptRegister() {
    const errorDiv = document.getElementById("error-div");
    const successDiv = document.getElementById("success-div");
    errorDiv.hidden = true;
    successDiv.hidden = true;

    // get input values from input fields
    const firstname = document.getElementById("firstname").value;
    const lastname = document.getElementById("lastname").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const role = document.getElementById("role").value;

    console.log(`firstname: ${firstname}, lastname: ${lastname}, email: ${email}, password: ${password}, role: ${role}`);

    const xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:8080/michelle-ers/register");

    // define onreadystatechange callback for the xhr object (check for readystate 4)
    xhr.onreadystatechange = function(){
        if(xhr.readyState===4){
            const respObj = JSON.parse(xhr.response);
            if(respObj.status) {
                 successDiv.hidden = false;
                 successDiv.innerText = respObj.message;
                 document.getElementById("register-form").reset();
            } else {
                errorDiv.hidden = false;
                errorDiv.innerText = respObj.message;
            }
        }
    }

    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    // send request, with the username and password in the request body
    const requestBody = `firstName=${firstname}&lastName=${lastname}&email=${email}&password=${password}&role=${role}`;
    xhr.send(requestBody);

}