window.addEventListener("load", getPendingRequest);

function getPendingRequest() {
    const errorDiv = document.getElementById("error-div");
    const successDiv = document.getElementById("success-div");
    errorDiv.hidden = true;
    successDiv.hidden = true;

    // get token from local storage
    var authToken = localStorage.getItem("token");

    const xhr = new XMLHttpRequest();
    xhr.open("GET", "http://localhost:8080/michelle-ers/reimbursement/fetch/pending");

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
                        td.innerHTML = sn;

                        for(key in respObj.data[i]) {
                            let td = tr.appendChild(document.createElement('td'));
                            td.title = key;
                            if(key == 'id') {
                                continue;
                            }
                            if(key === 'amount') {
                                td.innerHTML = '$ ' + (respObj.data[i][key]).toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,'); ;
                            } else {
                                td.innerHTML = respObj.data[i][key];
                            }
                        }

                        let actionTd = tr.appendChild(document.createElement('td'));

                        let approveLink = document.createElement('a');
                        approveLink.id = 'approve-link';
                        let approveLinkText = document.createTextNode("Approve");
                        approveLink.appendChild(approveLinkText);
                        approveLink.href = `http://localhost:8080/michelle-ers/static/manager/review-request.html?requestId=${respObj.data[i]['id']}&status=APPROVED`;
                        actionTd.appendChild(approveLink);

                        let declineLink = document.createElement('a');
                        declineLink.id = 'decline-link';
                        let declineLinkText = document.createTextNode("Deny");
                        declineLink.appendChild(declineLinkText);
                        declineLink.href = `http://localhost:8080/michelle-ers/static/manager/review-request.html?requestId=${respObj.data[i]['id']}&status=DENIED`;
                        actionTd.appendChild(declineLink);

                        sn++;
                    }

                } else {
                    errorDiv.hidden = false;
                    errorDiv.innerText = "No Pending Reimbursement Request Found";
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

