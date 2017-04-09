var seckill = {
    URL: {
        // 封装秒杀相关AJAX的URL
        now: function() {
            return '../../seckill/time/now';
        },
        exposer: function(seckillId) {
            return '../../seckill/' + seckillId + '/exposer';
        },
        execution: function(seckillId, md5) {
            return '../../seckill/' + seckillId + '/' + md5 + '/execution';
        }
    },
    handleSeckill: function(seckillId, seckillBox) {
        seckillBox.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
        $.post(seckill.URL.exposer(seckillId), {}, function(result) {
            if (result && result['success']) {
                var exposer = result['data'];
                if (exposer['exposed']) {
                    var md5 = exposer['md5'];
                    var killUrl = seckill.URL.execution(seckillId, md5);
                    console.log('killUrl: ' + killUrl);
                    // 只绑定一次点击事件
                    $('#killBtn').one('click', function() {
                        $(this).addClass('disabled');
                        $.post(killUrl, {}, function(result) {
                            if (result && result['success']) {
                                var killResult = result['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                seckillBox.html('<span class="label label-success">' + stateInfo + '</span>');
                            }
                        });
                    });
                    seckillBox.show();
                } else {
                    // 预防客户端浏览器计时偏差
                    var now = exposer['currentTime'];
                    var start = exposer['startTime'];
                    var end = exposer['endTime'];
                    seckill.countdown(seckillId, now, start, end);
                }
            } else {
                console.log('result: ' + result);
            }
        });
    },
    validatePhone: function(phone) {
        // if phone is undefined -> false
        if (phone && phone.length == 11 && !isNaN(phone))
            return true;
        return false;
    },
    countdown: function(seckillId, currentTime, startTime, endTime) {
        var seckillBox = $('#seckill-box');
        if (currentTime > endTime) {
            seckillBox.html('秒杀结束');
        } else if (currentTime < startTime) {
            var killTime = new Date(startTime + 1000);
            seckillBox.countdown(killTime, function(event) {
                var format = event.strftime('秒杀计时: %D天 %H时 %M分 %S秒');
                seckillBox.html(format);
            }).on('finish.countdown', function() {
                // 获取秒杀地址, 控制显示逻辑, 执行秒杀
                seckill.handleSeckill(seckillId, seckillBox);
            });
        } else {
            seckill.handleSeckill(seckillId, seckillBox);
        }
    },
    detail: {
        // 详情页秒杀逻辑
        init: function(params) {
            // 手机验证和登录, 计时交互
            // 规划交互流程
            var killPhone = $.cookie('userPhone');
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
            // 计时交互
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];
            $.get(seckill.URL.now(), {}, function(result) {
                if (result && result['success']) {
                    var currentTime = result['data'];
                    seckill.countdown(seckillId, currentTime, startTime, endTime);
                } else {
                    console.log('result: ' + result);
                }
            });
        }
    }
};
