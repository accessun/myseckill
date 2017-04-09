var seckill = {
    URL: {
        // 封装秒杀相关AJAX的URL

    },
    validatePhone: function(phone) {
        // if phone is undefined -> false
        if (phone && phone.length == 11 && !isNaN(phone))
            return true;
        return false;
    },
    detail: {
        // 详情页秒杀逻辑
        init: function(params) {
            // 手机验证和登录, 计时交互
            // 规划交互流程
            var killPhone = $.cookie('userPhone');
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];
            // 验证手机号
            if (!seckill.validatePhone(killPhone)) {
                // 绑定手机号
                var killPhoneModal = $('#killPhoneModal');
                killPhoneModal.modal({
                    show: true, // show modal
                    backdrop: 'static', // disable position close
                    keyboard: false // disble keyboard event to close
                });
                $('#killPhoneBtn').click(function(event) {
                    var inputPhone = $('#killPhoneKey').val();
                    if (seckill.validatePhone(inputPhone)) {
                        $.cookie('userPhone', inputPhone, {expires:7});
                        window.location.reload();
                    } else {
                        $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误</label>').show(300);
                    }
                });
            }

        }
    }
};
