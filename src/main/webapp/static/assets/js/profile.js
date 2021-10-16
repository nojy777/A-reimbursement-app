window.addEventListener("load", getProfile);

function getProfile() {
    const errorDiv = document.getElementById("error-div");
    const successDiv = document.getElementById("success-div");
     const profileBlock = document.getElementById("profile-block");
    errorDiv.hidden = true;
    successDiv.hidden = true;
    profileBlock.hidden = true;

    // get token from local storage
    var authToken = localStorage.getItem("token");

    const xhr = new XMLHttpRequest();
    xhr.open("GET", "http://localhost:8080/michelle-ers/profile");

    // define onreadystatechange callback for the xhr object (check for readystate 4)
    xhr.onreadystatechange = function(){
        if(xhr.readyState===4){
            const respObj = JSON.parse(xhr.response);
            console.log(respObj);
            if(respObj.status) {
                profileBlock.hidden = false;
                document.getElementById("firstname").innerHTML = respObj.data.firstName;
                document.getElementById("lastname").innerHTML = respObj.data.lastName;
                document.getElementById("email").innerHTML = respObj.data.email;
                document.getElementById("role").innerHTML = respObj.data.role;
            } else {
                errorDiv.hidden = false;
                errorDiv.innerText = respObj.message;
            }
        }
    }

    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhr.setRequestHeader("Authorization", authToken);
    xhr.send();

}