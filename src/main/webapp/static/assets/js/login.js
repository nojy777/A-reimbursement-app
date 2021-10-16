document.getElementById('login-btn').addEventListener("click", attemptLogin);

function attemptLogin() {
    const errorDiv = document.getElementById("error-div");
    errorDiv.hidden = true;

    // get input values from input fields
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    console.log(`email: ${email}, password: ${password}`);

    const xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:8080/michelle-ers/login");

    // define onreadystatechange callback for the xhr object (check for readystate 4)
    xhr.onreadystatechange = function(){
        if(xhr.readyState===4){
            const respObj = JSON.parse(xhr.response);
            if(respObj.status) {
                const token = respObj.data.token;
                const role = respObj.data.role;
                localStorage.setItem("token", token);
                localStorage.setItem("role", role);
                if(role == "MANAGER") {
                    window.location.href="http://localhost:8080/michelle-ers/static/manager/home.html";
                } else {
                    window.location.href="http://localhost:8080/michelle-ers/static/employee/home.html";
                }
            } else {
                errorDiv.hidden = false;
                errorDiv.innerText = respObj.message;
            }
        }
    }

    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    // send request, with the username and password in the request body
    const requestBody = `email=${email}&password=${password}`;
    xhr.send(requestBody);

}