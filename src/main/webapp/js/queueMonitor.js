// 生成 各队列的统计表格
var createQueueCharts = function(data,isProd){
    //append data
    var $tbody = $('.g2-table').children('tbody');
    var index = 0;
    var CERBERUS_DOMAIN = "http://cerberus.corp.qunar.com";
    var QSCHEDULE_DOMAIN = "http://qschedule.corp.qunar.com";
    if(!isProd){
        CERBERUS_DOMAIN = "http://cerberus.beta.qunar.com";
        QSCHEDULE_DOMAIN = "http://beta.qschedule.corp.qunar.com";
    }
    var cerberus_prefix = CERBERUS_DOMAIN +"/full_new_metric.htm?appCode=vs_fh_product_service&name=";
    data.forEach(function (d, index) {
        //create container
        var $tr = $('<tr class="g2-table-row"></tr>');
        $tbody.append($tr);
        // 序号
        var $index = $('<td class="g2-table-column g2-table-column-index">' + (index++) + '</td>');
        $tr.append($index);
        // 队列名称
        var $id = $('<td class="g2-table-column">' + d.queueName + '</td>');
        $tr.append($id);
        // 队列实时大小
        var $time = $('<td class="g2-table-column">'+d.queueSize+'</td>');
        $tr.append($time);
        // 当日总接受量
        var $totalRecvSize = $('<td class="g2-table-column g2-table-column-index">'+d.totalRecvSize+'</td>');
        $tr.append($totalRecvSize);
        // 当日去重数量
        var $duplicateSize = $('<td class="g2-table-column g2-table-column-index">'+d.duplicateSize+'</td>')
        $tr.append($duplicateSize);
        // Cerberus监控 http://cerberus.corp.qunar.com/full_new_metric.htm?appCode=vs_fh_product_service&name=private_redis_queue_size_hp_hotel_price_update_queue_12345_low_level
        var $cerberusLink = $('<td class="g2-table-column g2-table-column-index"><a href="'+ cerberus_prefix+d.cerberusKey +'" target="_blank">Cerberus</a></td>');
        $tr.append($cerberusLink);
        // 队列实际消费监控
        var $cerberusQueueConsumeLink = $('<td class="g2-table-column g2-table-column-index"><a href="'+CERBERUS_DOMAIN+'/full_new_metric.htm?appCode=vs_fh_product_service&name=price_engine_cal_price_item_count" target="_blank">Cerberus</a></td>');
        $tr.append($cerberusQueueConsumeLink);
        // 队列消费时长监控
        var $cerberusQueueConsumeTimeLink = $('<td class="g2-table-column g2-table-column-index"><a href="'+CERBERUS_DOMAIN+'/full_new_metric.htm?appCode=vs_fh_product_service&name=price_engine_cal_price_item_time" target="_blank">Cerberus</a></td>');
        $tr.append($cerberusQueueConsumeTimeLink);
        // 并发数设置
        var $rateLimitLink = $('<td class="g2-table-column g2-table-column-index"><a href="'+QSCHEDULE_DOMAIN+'/jobs/edit.do?jobName=hotel_price_queue_consume_task" target="_blank">QSchedule</a></td>');
        $tr.append($rateLimitLink);
        // 预览
        var $queryQueueLink = $('<td class="g2-table-column g2-table-column-index"><a href="/api/debug/price/queue/zrangePriceQueue?start=0&end=10&key='+d.queueName +'" target="_blank">预览前十条</a></td>');
        $tr.append($queryQueueLink);
        // 清空队列
        var $resetQueueLink = $('<td class="g2-table-column g2-table-column-index"><a href="/api/debug/price/queue/delAllPriceQueue?confirm=true&queueKey='+d.queueName +'" target="_blank">清空</a></td>');
        $tr.append($resetQueueLink);
    });


};

// 生成 去重数量效果饼状图
var createTotalSizeCharts = function(data){
    var ds = new DataSet();
    var dv = ds.createView().source(data);
    dv.transform({
        type: 'percent',
        field: 'size',
        dimension: 'type',
        as: 'percent'
    });

    var chart = new G2.Chart({
        container: 'totalSizeChartsContainer',
        forceFit: true,
        height: window.innerHeight,
        padding: 14
    });
    chart.source(dv);
    chart.legend(false);
    chart.coord('theta', {
        radius: 0.75
    });
    chart.intervalStack().position('size').color('type', ['#2593fc', '#38c060', '#27c1c1', '#705dc8', '#3b4771', '#f9cb34']).opacity(1).label('value', function(val) {
        var offset = val > 0.02 ? -30 : 30;
        var label_class = val > 0.02 ? "g2-label-item-inner" : "g2-label-item-outer";
        return {
            offset: offset,
            useHtml: true,
            htmlTemplate: function htmlTemplate(text, item) {
                var d = item.point;
                var percent = String((d.percent * 100).toFixed(2)) + "%";
                return '<div class=' + label_class + '>' + d.showName + percent + '</div>';
            }
        };
    });
    chart.render();
};

// 获取队列相关的统计数据
var getPriceQueueStatisticsData = function () {
    // var statisticsData = {"isProd":true,"queueData":[{"queueName":"hp:hotel_price_update_queue:10356:high_level","queueSize":6,"duplicateSize":0,"totalRecvSize":0,"totalConsumeSize":0,"rate":null,"cerberusKey":"private_redis_queue_size_hp:hotel_price_update_queue:10356:high_level"},{"queueName":"hp:hotel_price_update_queue:12345:high_level","queueSize":4,"duplicateSize":0,"totalRecvSize":0,"totalConsumeSize":0,"rate":null,"cerberusKey":"private_redis_queue_size_hp:hotel_price_update_queue:12345:high_level"},{"queueName":"hp:hotel_price_update_queue:hds_china:low_level","queueSize":468,"duplicateSize":0,"totalRecvSize":0,"totalConsumeSize":0,"rate":null,"cerberusKey":"private_redis_queue_size_hp:hotel_price_update_queue:hds_china:low_level"},{"queueName":"hp:hotel_price_update_queue:czk:high_level","queueSize":3,"duplicateSize":0,"totalRecvSize":0,"totalConsumeSize":0,"rate":null,"cerberusKey":"private_redis_queue_size_hp:hotel_price_update_queue:czk:high_level"},{"queueName":"hp:hotel_price_update_queue:18675:low_level","queueSize":12,"duplicateSize":0,"totalRecvSize":0,"totalConsumeSize":0,"rate":null,"cerberusKey":"private_redis_queue_size_hp:hotel_price_update_queue:18675:low_level"},{"queueName":"hp:hotel_price_update_queue:54321:high_level","queueSize":1,"duplicateSize":0,"totalRecvSize":0,"totalConsumeSize":0,"rate":null,"cerberusKey":"private_redis_queue_size_hp:hotel_price_update_queue:54321:high_level"}],"totalData":[{"size":150,"type":"consumeSize","showName":"队列已取出数量"},{"size":20,"type":"dupSize","showName":"队列去重数量"},{"size":0,"type":"inQueueSize","showName":"队列中数量"}]};
    var statisticsData = {};
    $.ajax({
        url: "/api/debug/price/queue/getPriceQueueStatistics",
        method: "GET",
        async: false,
        success: function (res) {
            if(res.ret != true){
                alert("队列相关统计数据接口返回结果不合法，请检查/api/debug/price/queue/getPriceQueueStatistics接口");
                return;
            }
            statisticsData = res.data;
        }
    });
    return statisticsData;
};

var statisticsData = getPriceQueueStatisticsData();
if(!$.isEmptyObject(statisticsData.queueData)){
    createQueueCharts(statisticsData.queueData,statisticsData.isProd);
}
if(!$.isEmptyObject(statisticsData.totalData)){
    createTotalSizeCharts(statisticsData.totalData);
}
