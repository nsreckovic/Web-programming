<%-- Created by IntelliJ IDEA. --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
  <!-- Required meta tags -->
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

  <!-- Bootstrap CSS -->
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">

  <title>WebAir</title>

</head>
<body>
<!-- ADD TICKET FORM -->
<div class="container mt-5 pt-2 pb-3 border border-secondary rounded">
  <h2>Add ticket</h2>

  <br>

  <div id="form-date-alert" class="alert alert-danger collapse">
    <a class="close" data-dismiss="alert">&times;</a>
    <strong>Error!</strong> Make sure that return date is after departure date and try again.
  </div>

  <div id="form-place-alert" class="alert alert-danger collapse">
    <a class="close" data-dismiss="alert">&times;</a>
    <strong>Error!</strong> Make sure the places From and To are different and try again.
  </div>

  <form id="add-ticket-form">

    <div class="row">
      <div class="col">
        <div class="form-group">
          <label for="from">From:</label>
          <input type="text" class="form-control" name="from" id="from" placeholder="From" required>
        </div>
      </div>

      <div class="col">
        <div class="form-group">
          <label for="to">To:</label>
          <input type="text" class="form-control" name="to" id="to" placeholder="To" required>
        </div>
      </div>
    </div>

    <div class="row">
      <div class="col">
        <label>Departure date:</label>
        <input type="date" name="departure-date" id="departure-date" min="2020-01-01" max="2023-12-31" class="form-control" required>
      </div>

      <div class="col">
        <label>Return date:</label>
        <input type="date" name="return-date" id="return-date" min="2020-01-01" max="2023-12-31" class="form-control">
      </div>
    </div>

    <div class="row mt-3">
      <div class="col">
        <label>Company:</label>
        <select class="custom-select" name="company" id="company" required>
          <option selected>Choose...</option>
        </select>
      </div>

      <div class="col">
        <%--@declare id="dummy"--%><label for="dummy"></label>
        <div class="custom-control custom-checkbox mr-sm-2 mt-3">
          <input type="checkbox" class="custom-control-input" name="one-way" id="one-way" onclick="one_way_checked(this)">
          <label class="custom-control-label" for="one-way">One-way ticket</label>
        </div>
      </div>
    </div>

    <br>

    <button type="submit" class="btn btn-primary btn-lg btn-block">Submit</button>
  </form>
</div>

<div class="container mt-5 pt-2 pb-3 border border-secondary rounded">

  <div class="dropdown">
    <button class="btn btn-primary dropdown-toggle mt-3 mb-2" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
      <span>All tickets</span>
    </button>
    <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
      <a class="dropdown-item" href="#" id="all_tickets">All tickets</a>
      <a class="dropdown-item" href="#" id="one_way_tickets">One-way tickets</a>
      <a class="dropdown-item" href="#" id="round_trip_tickets">Round-trip tickets</a>
    </div>
  </div>

  <br>

  <div class="table-responsive">
    <table class="table table-bordered border-secondary rounded" id="ticket-table">
      <thead>
      <tr>
        <th scope="col">One-way</th>
        <th scope="col">From</th>
        <th scope="col">To</th>
        <th scope="col">Departure date</th>
        <th scope="col">Return date</th>
        <th scope="col">Company</th>
      </tr>
      </thead>

      <tbody>

      </tbody>
    </table>
  </div>

</div>

<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
<script src="js/script.js"></script>
</body>
</html>