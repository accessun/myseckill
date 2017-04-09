<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="common/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="common/head.jsp" %>
<title>秒杀详情页</title>
</head>
<body>

  <div class="container">
    <div class="panel panel-default text-center">
      <div class="panel-heading">
        <h1>${seckill.name}</h1>
      </div>
      <div class="panel-body">
          <h2 class="text-danger">
            <span class="glyphicon glyphicon-time"></span>
            <span class="glyphicon" id="seckill-box"></span>
          </h2>
      </div>
    </div>
  </div>

  <!-- modal for user input phone number -->
  <div id="killPhoneModal" class="modal fade">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h3 class="modal-title text-center">
            <span class="glyphicon glyphicon-phone"></span>秒杀电话:
          </h3>
        </div>
        <div class="modal-body">
          <div class="row">
            <div class="col-xs-8 col-xs-offset-2">
              <input type="text" name="killPhone" id="killPhoneKey"  placeholder="Your phone number" class="form-control">
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <span id="killPhoneMessage" class="glyphicon"></span>
          <button type="button" id="killPhoneBtn" class="btn btn-success">
            <span class="glyphicon glyphicon-phone"></span>
            Submit
          </button>
        </div>
      </div>
    </div>
  </div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.countdown/2.2.0/jquery.countdown.min.js"></script>
<script src="../../assets/scripts/seckill.js"></script>
<script>
    $(function() {
        seckill.detail.init({
            seckillId: ${seckill.seckillId},
            startTime: ${seckill.startTime.time}, // in milliseconds
            endTime: ${seckill.endTime.time}
        });
    });
</script>
</body>
</html>
