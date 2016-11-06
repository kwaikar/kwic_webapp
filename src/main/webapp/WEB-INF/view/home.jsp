<%@ include file="include.jsp"%>
<html>
<head>
<title>KWIC Indexing System</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://cdn.datatables.net/1.10.12/css/dataTables.bootstrap.min.css">


<link rel="stylesheet" href="resources/css/style.css">
<script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>

<script
	src="https://cdn.datatables.net/1.10.12/js/jquery.dataTables.min.js"></script>
<script
	src="https://cdn.datatables.net/1.10.12/js/dataTables.bootstrap.min.js"></script>

<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		$('#searchResults').DataTable();
	});
</script>
<head>
<body>
	<div class="panel panel-info center"
		style="width: 1000px; text-align: center; margin-top: 50px">
		<div class="panel-heading" style="text-align: center">
			<div>
				<h3>Kwic Indexing System</h3>
			</div>
			<c:if test="${message!=null}">
				<div class="row" style="color: #ff0000; text-align: left">
					<B>${message}</B>
				</div>
			</c:if>
		</div>
		<div class="panel-body">
			<c:if test="${user==null}">

				<div class="panel panel-info center"
					style="width: 400px; text-align: center; margin-top: 50px">
					<div class="panel-heading">
						<div>
							<h3>Login</h3>
						</div>


					</div>
					<div class="panel-body">
						<form name="loginForm" id="loginForm" action="login" method="post">
							<div class="row" style="padding-top: 10px">
								<div class="col-xs-6">Email</div>
								<div class="col-xs-6">
									<input type="text" required class="form-control"
										name="userName" id="userName" value="kwicuser" />
								</div>
							</div>
							<div class="row" style="padding-top: 10px">
								<div class="col-xs-6">Password</div>
								<div class="col-xs-6">
									<input type="password" class="form-control" required
										id="password" name="password" value="utd123" />
								</div>
							</div>
							<div class="row" style="padding: 10px">
								<div class="col-xs-12" style="text-align: right">
									<input id="loginBtn" name="loginBtn" class="btn btn-info"
										type="submit" value="Login!" />
								</div>
							</div>
						</form>
					</div>
				</div>
			</c:if>

			<c:if test="${user!=null}">
				<div class="panel-heading">
					<div align="right">
						<c:if test="${indexPresent!=null}">
							<a href="deleteIndex">delete Index </a> | 
					</c:if>
						<a href="logout">logout</a>
					</div>
				</div>
				<div class="panel-body">
					<form name="loginForm" id="loginForm" action="index" method="get">
						<div class="row" style="padding-top: 10px">
							<div class="col-xs-4">
								<h4>URL to be indexed</h4>
							</div>
							<div class="col-xs-4">
								<input type="text" required class="form-control" name="indexURL"
									id="indexURL"
									value="https://raw.githubusercontent.com/kwaikar/Statistical-Opinion-Polarity-Detection/master/src/main/resources/input_test2" />
							</div>
							<div class="col-xs-4" style="text-align: right">
								<input id="loginBtn" name="loginBtn" class="btn btn-info"
									type="submit" value="Generate Kwic-Index Please" />
							</div>
						</div>
					</form>
					<c:if test="${indexPresent!=null}">
						<form name="searchForm" id="searchForm" action="search"
							method="post">
							<div class="row" style="padding-top: 10px">
								<div class="col-xs-4">
									<h4>Search:</h4>
								</div>
								<div class="col-xs-4">
									<input type="text" required class="form-control" name="prefix"
										id="prefix" value="" />
								</div>
								<div class="col-xs-4" style="text-align: right">
									<input id="loginBtn" name="loginBtn" class="btn btn-info"
										type="submit" value="Search" />
								</div>
							</div>
						</form>
					</c:if>
					<c:if test="${searchResults!=null}">
						<div class="well well-sm" style="text-align: center">
							<h4>Search Results</h4>
						</div>
								<table class="table table-striped table-bordered" id="searchResults" width="100%">
									<thead>
										<tr>
											<th>S.No.</th>
											<th>URL</th>
											<th>Description</th>
											<th>Priority</th>
										</tr>
									</thead>
									<c:forEach items="${searchResults}" var="entry" varStatus="cnt">
										<tr>
											<td>${cnt.count}</td>
											<td><a target="_new" href="${entry.url}">${entry.url}</a></td>
											<td>${entry.originalString}</td>
											<td>${entry.priority}</td>
										</tr>
									</c:forEach>
								</table>
					</c:if>

				</div>
			</c:if>

		</div>
	</div>
</body>
</html>