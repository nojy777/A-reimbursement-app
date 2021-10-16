window.addEventListener("load", getAllEmployees);

function getAllEmployees() {
    const errorDiv = document.getElementById("error-div");
    const successDiv = document.getElementById("success-div");
    errorDiv.hidden = true;
    successDiv.hidden = true;

    // get token from local storage
    var authToken = localStorage.getItem("token");

    const xhr = new XMLHttpRequest();
    xhr.open("GET", "http://localhost:8080/michelle-ers/manager/employees");

    // define onreadystatechange callback for the xhr object (check for readystate 4)
    xhr.onreadystatechange = function(){
        if(xhr.readyState===4){
            const respObj = JSON.parse(xhr.response);
            if(respObj.status) {
                if(respObj.data.length > 0) {
                    const myTable = document.getElementById('myTable');
                    let sn = 1;
                    for(i in respObj.data) {
                        let tr = myTable.appendChild(document.createElement('tr'));

                        // sn
                        let td = tr.appendChild(document.createElement('td'));
                        // td.title = "";
                        td.innerHTML = sn;

                        for(key in respObj.data[i]) {
                            if(key == 'id'){
                                continue;
                            }
                            let td = tr.appendChild(document.createElement('td'));
                            td.title = key;
                            td.innerHTML = respObj.data[i][key];
                        }
                        sn++;
                    }
                } else {
                    // no resolved request found
                    errorDiv.hidden = false;
                    errorDiv.innerText = "No Employee Found";
                    return;
                }

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