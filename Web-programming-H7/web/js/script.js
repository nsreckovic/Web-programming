'use strict';

window.onload = function () {
    loadCompanies()
    loadTickets()
}

function loadCompanies() {
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            let result = JSON.parse(this.response);

            var dynamicSelect = document.getElementById("company");

            for (var i = 0; i < result.length; i++) {
                var newOption = document.createElement("option");
                newOption.text = result[i].name;
                dynamicSelect.add(newOption);
            }

        }
    };

    xhttp.open("GET", "/webair/companies", true);
    xhttp.send();
}

function loadTickets(url) {
    if (url == null) url = String('/webair/tickets');
    console.log(url);
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            let result = JSON.parse(this.response);

            let table = document.getElementById("ticket-table");

            let oldTBody = table.tBodies[0];
            let newTBody = document.createElement("tBody");

            for (let i = 0; i < result.length; i++) {

                let bRow = document.createElement("tr"); // Kreiraj novi red

                let td_one_way = document.createElement("td");
                td_one_way.innerHTML = result[i].one_way;
                let td_from = document.createElement("td");
                td_from.innerHTML = result[i].from;
                let td_to = document.createElement("td");
                td_to.innerHTML = result[i].to;
                let td_departure_date = document.createElement("td");
                td_departure_date.innerHTML = result[i].departure_date;
                let td_return_date = document.createElement("td");
                td_return_date.innerHTML = result[i].return_date;
                let td_company = document.createElement("td");
                td_company.innerHTML = '<a href="' + result[i].company_link + '" target="_blank">' + result[i].company + '</a>'

                bRow.appendChild(td_one_way);
                bRow.appendChild(td_from);
                bRow.appendChild(td_to);
                bRow.appendChild(td_departure_date);
                bRow.appendChild(td_return_date);
                bRow.appendChild(td_company);

                newTBody.appendChild(bRow)
            }

            table.replaceChild(newTBody, oldTBody)
        }
    };

    xhttp.open("GET", url, true);
    xhttp.send();
}

$(document).ready(function () {
    var form = document.getElementById('add-ticket-form');
    if (form.attachEvent) form.attachEvent("submit", process_form);
    else form.addEventListener("submit", process_form);

    // Dropdown ticket filter
    $('.dropdown a#all_tickets').on("click", function (e) {
        $('#dropdownMenuButton').text('All tickets').button("refresh");
        let url = String('/webair/tickets');
        loadTickets(url);
        e.preventDefault();
    });
    $('.dropdown a#one_way_tickets').on("click", function (e) {
        $('#dropdownMenuButton').text('One-way tickets').button("refresh");
        let url = String('/webair/tickets/one-way');
        loadTickets(url);
        e.preventDefault();
    });
    $('.dropdown a#round_trip_tickets').on("click", function (e) {
        $('#dropdownMenuButton').text('Round-trip tickets').button("refresh");
        let url = String('/webair/tickets/round-trip');
        loadTickets(url);
        e.preventDefault();
    });

    $(".alert").find(".close").on("click", function (e) {
        e.stopPropagation();    // Don't allow the click to bubble up the DOM
        e.preventDefault();    // Don't let any default functionality occur (in case it's a link)
        $(this).closest(".alert").hide('fade')
    });
});

function process_form(e) {
    if (e.preventDefault) e.preventDefault();
    let formData = new FormData(e.target);

    let from = formData.get("from").toUpperCase();
    let to = formData.get("to").toUpperCase();
    if (from.localeCompare(to) == 0) {
        console.log("noup")
        $("#form-place-alert").show('fade')
        return false;
    } else {
        console.log("yes")
        $("#form-place-alert").hide('fade')
    }
    let departure_date = formData.get("departure-date");
    let return_date = formData.get("return-date");
    let dateDeparture = new Date($('input[name="departure-date"]').val()); // or Date.parse(...)
    let dateReturn = new Date($('input[name="return-date"]').val()); // or Date.now()
    if (dateDeparture.getTime() > dateReturn.getTime()) {
        $("#form-date-alert").show('fade')
        return false;
    } else {
        $("#form-date-alert").hide('fade')
    }
    let company = formData.get("company");
    let one_way = formData.get("one-way");
    if (one_way == "on" || return_date == '') one_way = true;
    else one_way = false;

    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            console.log(this.response);
            let url = String('/webair/tickets');
            loadTickets(url);
        }
    };
    xhttp.open("POST", "/webair/tickets", true);
    xhttp.setRequestHeader("Content-Type", "application/json");
    xhttp.send(JSON.stringify({
        from: from,
        to: to,
        departure_date: departure_date,
        return_date: return_date,
        company: company,
        one_way: one_way
    }));
    return false;
}

function one_way_checked(termsCheckBox) {
    if (termsCheckBox.checked) {
        document.getElementById("return-date").disabled = true;
        document.getElementById("return-date").value = '';
    } else {
        document.getElementById("return-date").disabled = false;
    }
}



