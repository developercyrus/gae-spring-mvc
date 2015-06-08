<%@ page import="java.text.Format"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.GregorianCalendar"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%
    Format format = new SimpleDateFormat("yyyy");
    Calendar cal = new GregorianCalendar();
    cal.setTime(new Date());
%>
<html>
	<head>
		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
		<script type="text/javascript" src="https://github.com/douglascrockford/JSON-js/raw/master/json2.js"></script>
		<script type="text/javascript" src="http://form2js.googlecode.com/hg/src/form2object.js"></script>
		<script type="text/javascript" src="js/gae/math/lp/jquery.blockUI.js"></script>
		<script type="text/javascript" src="js/gae/math/lp/ui.control.js"></script>
		<link type="text/css" rel="stylesheet" href="css/gae/math/lp/clearfix.css">
		<link type="text/css" rel="stylesheet" href="css/gae/math/lp/main.css">
	</head>
	<body>
		<div>
			An <a id="example-link" href="#">example</a> from <a href="http://en.wikipedia.org/wiki/Simplex_method#Example" target="_blank">Wikipedia</a> for Linear Programming.
		</div>
		<div class="example">
			<br /> <img
				src="http://chart.googleapis.com/chart?cht=tx&chs=12&chl={\mathrm{minimize}}" />
			<br /> <img
				src="http://chart.googleapis.com/chart?cht=tx&chs=15&chl=\quad\quad\quad\quad\quad\quad\quad\quad\quad\quad f=-2x_0-3x_1-4x_2 %2B 0" />
			<br /> <img
				src="http://chart.googleapis.com/chart?cht=tx&chs=15&chl={\mathrm{subject}}\quad{\mathrm{to}}" />
			<br /> <img
				src="http://chart.googleapis.com/chart?cht=tx&chs=15&chl=\quad\quad\quad\quad\quad\quad\quad\quad\quad\quad 3x_0 %2B 2x_1 %2B 1x_2 \leq 10" />
			<br /> <img
				src="http://chart.googleapis.com/chart?cht=tx&&chs=15&chl=\quad\quad\quad\quad\quad\quad\quad\quad\quad\quad 2x_0 %2B 5x_1 %2B 3x_2 \leq 15" />
			<br /> <img
				src="http://chart.googleapis.com/chart?cht=tx&&chs=15&chl=\quad\quad\quad\quad\quad\quad\quad\quad\quad\quad 1x_0 %2B 0x_1 %2B 0x_2 \geq 0" />
			<br /> <img
				src="http://chart.googleapis.com/chart?cht=tx&&chs=15&chl=\quad\quad\quad\quad\quad\quad\quad\quad\quad\quad 0x_0 %2B 1x_1 %2B 0x_2 \geq 0" />
			<br /> <img
				src="http://chart.googleapis.com/chart?cht=tx&&chs=15&chl=\quad\quad\quad\quad\quad\quad\quad\quad\quad\quad 0x_0 %2B 0x_1 %2B 1x_2 \geq 0" />
			<br /> <br />
	
		</div>
		<div id="param">
			<div id="userInput" class="clearfix">
				<label>Please select the no. of variables:</label> <select id="varNo"
					name="varcount">
					<option value="-1">select</option>
					<option value="2">2</option>
					<option value="3">3</option>
					<option value="4">4</option>
					<option value="5">5</option>
					<option value="6">6</option>
					<option value="7">7</option>
					<option value="8">8</option>
					<option value="9">9</option>
					<option value="10">10</option>
				</select>
			</div>
			<div id="equation" class="clearfix"></div>
		</div>
		<div id="control" class="clearfix control">
			<button id="addMore">add more</button>
			<button id="calculate">calculate</button>
		</div>
		<pre id="returned"></pre>
		<div id="solDetail"></div>
		<div id="preload">
			<img style="display: none" src="pic/gae/math/lp/ajax-loader.gif">
		</div>
		<div id="contact">
			© 2009-<%=format.format(cal.getTime())%>
			<a href="mailto:developer.cyrus@gmail.com">cyrus</a>
		</div>
	</body>
</html>
