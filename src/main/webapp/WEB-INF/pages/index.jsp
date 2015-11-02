<%--
  Created by IntelliJ IDEA.
  User: 264929
  Date: 2015/7/31
  Time: 9:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="utf-8">
    <title></title>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/semantic/semantic.min.css"/>">
    <script src="<c:url value="/resources/eCharts/echarts.js"/> "></script>
    <script src="<c:url value="/resources/jQuery/jquery-2.1.4.min.js"/> "></script>
    <script src="<c:url value="/resources/datejs/date.js"/> "></script>
    <script src="<c:url value="/resources/semantic/semantic.min.js"/> "></script>
    <script type="text/javascript">
        require.config({
            paths: {
                echarts: '<c:url value="/resources/eCharts"/>'
            }
        });

        var memoryChart, statChart, networkChart;

        var holdData = [];

        var Config = {
            url: '<c:url value="/fetch"/>',
            TimeSpan: {
                REAL_TIME: 'REAL_TIME',
                ONE_HOUR: 'ONE_HOUR',
                TWO_HOUR: 'TWO_HOUR',
                THREE_HOUR: 'THREE_HOUR',
                FOUR_HOUR: 'FOUR_HOUR',
                TODAY: 'FOUR_HOUR',
                YESTERDAY: 'FOUR_HOUR'
            }
        };

        var Data = {
            memoryPeak: [],
            memoryCur: [],
            dateList: [],
            cmdProcessed: [],
            totalCnnRev: [],
            totalNetIn: [],
            totalNetOut: [],
            expiredKeys: [],
            evictedKeys: []
        };

        var timer;
        var timeSpan = Config.TimeSpan.REAL_TIME;

        function drawMemoryChart(ec) {
            memoryChart = ec.init(document.getElementById('memory'));
            var option = {
                title: {
                    text: 'Memory Usage',
                    subtext: '内存使用'
                },
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data: ['Peak', 'Current']
                },
                calculable: true,
                xAxis: [
                    {
                        type: 'category',
                        boundaryGap: false,
                        data: ['0', '0', '0', '0', '0', '0', '0']
                    }
                ],
                yAxis: [
                    {
                        type: 'value',
                        axisLabel: {
                            formatter: '{value} Mb'
                        }
                    }
                ],
                series: [
                    {
                        name: 'Peak Memory',
                        type: 'line',
                        data: [0, 0, 0, 0, 0, 0, 0],
                        markPoint: {
                            data: [
                                {type: 'max', name: 'Peak'},
                                {type: 'min', name: 'Current'}
                            ]
                        },
                        markLine: {
                            data: [
                                {type: 'average', name: 'Average'}
                            ]
                        }
                    },
                    {
                        name: 'Current',
                        type: 'line',
                        data: [0, 0, 0, 0, 0, 0, 0],
                        markPoint: {
                            data: [
                                {name: 'Current', value: 0}
                            ]
                        },
                        markLine: {
                            data: [
                                {type: 'average', name: 'average'}
                            ]
                        }
                    }
                ],
//                addDataAnimation:false,
                animation: false
            };
            memoryChart.setOption(option);
            layOut('memory');
        }

        function drawStatChart(ec) {
            statChart = ec.init(document.getElementById('stat'));
            var option = {
                title: {
                    text: 'STAT',
                    subtext: '状态'
                },
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data: ['Total Command Processed', 'Total Connections Received']
                },
                calculable: true,
                xAxis: [
                    {
                        type: 'category',
                        boundaryGap: false,
                        data: ['0', '0', '0', '0', '0', '0', '0']
                    }
                ],
                yAxis: [
                    {
                        type: 'value'
                    }
                ],
                series: [
                    {
                        name: 'Total Command Processed',
                        type: 'line',
                        smooth: true,
                        itemStyle: {normal: {areaStyle: {type: 'default'}}},
                        data: [0, 0, 0, 0, 0, 0, 0],
                        markPoint: {
                            data: [
                                {type: 'max', name: 'Peak'}
                            ]
                        }
                    },
                    {
                        name: 'Total Connections Received',
                        type: 'line',
                        smooth: true,
                        itemStyle: {normal: {areaStyle: {type: 'default'}}},
                        data: [0, 0, 0, 0, 0, 0, 0],
                        markPoint: {
                            data: [
                                {type: 'max', name: 'Peak'}
                            ]
                        }
                    },
                    {
                        name: 'Expired keys',
                        type: 'line',
                        smooth: true,
                        itemStyle: {normal: {areaStyle: {type: 'default'}}},
                        data: [0, 0, 0, 0, 0, 0, 0],
                        markPoint: {
                            data: [
                                {type: 'max', name: 'Peak'}
                            ]
                        }
                    },
                    {
                        name: 'Evicted keys',
                        type: 'line',
                        smooth: true,
                        itemStyle: {normal: {areaStyle: {type: 'default'}}},
                        data: [0, 0, 0, 0, 0, 0, 0],
                        markPoint: {
                            data: [
                                {type: 'max', name: 'Peak'}
                            ]
                        }
                    }
                ],
                animation: false
            };
            statChart.setOption(option);
            layOut('stat');
        }

        function drawNetworkChart(ec) {
            networkChart = ec.init(document.getElementById('network'));
            var option = {
                title: {
                    text: 'NetWork',
                    subtext: '状态'
                },
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data: ['Total Net Input', 'Total Net Output']
                },
                calculable: true,
                xAxis: [
                    {
                        type: 'category',
                        boundaryGap: false,
                        data: ['0', '0', '0', '0', '0', '0', '0']
                    }
                ],
                yAxis: [
                    {
                        type: 'value',
                        axisLabel: {
                            formatter: '{value} kb'
                        }
                    }
                ],
                series: [
                    {
                        name: 'Total Net Input',
                        type: 'line',
                        smooth: true,
                        itemStyle: {normal: {areaStyle: {type: 'default'}}},
                        data: [0, 0, 0, 0, 0, 0, 0],
                        markPoint: {
                            data: [
                                {type: 'max', name: 'Peak'}
                            ]
                        }
                    },
                    {
                        name: 'Total Net Output',
                        type: 'line',
                        smooth: true,
                        itemStyle: {normal: {areaStyle: {type: 'default'}}},
                        data: [0, 0, 0, 0, 0, 0, 0],
                        markPoint: {
                            data: [
                                {type: 'max', name: 'Peak'}
                            ]
                        }
                    }
                ],
                animation: false
            };
            networkChart.setOption(option);
            layOut('network');
        }


        require(
                [
                    'echarts',
                    'echarts/chart/line',
                    'echarts/chart/bar'
                ],
                function (ec) {
                    fetchServerData(ec, timeSpan, drawCharts);

                    timer = setInterval(function () {
                        fetchServerData(ec, timeSpan, drawCharts);
                    }, 5000);
                }
        );

        function drawCharts(ec) {
            drawMemoryChart(ec);
            drawStatChart(ec);
            drawNetworkChart(ec);
        }


        function clearData() {
            Data.memoryCur = [];
            Data.memoryPeak = [];
            Data.dateList = [];
            Data.cmdProcessed = [];
            Data.totalCnnRev = [];
            Data.totalNetIn = [];
            Data.totalNetOut = [];
            Data.expiredKeys = [];
            Data.evictedKeys = [];
        }

        function timeConvert(num) {
            var dayFormat = 'yyyy-MM-dd';
            var timeFormat = 'HH:mm:ss';
            var date = (new Date(num));
            var today = new Date();
            if (date.toString(dayFormat) == today.toString(dayFormat)) {
                return date.toString(timeFormat);
            }
            return date.toString(dayFormat + ' ' + timeFormat);
        }

        function layOut(type) {
            if (!holdData.length) return;
            var option;
            for (var i = 0; i < holdData.length; i++) {
                var info = holdData[i].info;
                var date = holdData[i].date;
                Data.dateList.push(timeConvert(date));
                Data.memoryPeak.push(info.usedMemoryPeak / (1024 * 1024));
                Data.memoryCur.push(info.usedMemory / (1024 * 1024));
                Data.cmdProcessed.push(info.totalCommandsProcessed);
                Data.totalCnnRev.push(info.totalConnectionsReceived);
                Data.totalNetIn.push(info.totalNetInputBytes / (1024));
                Data.totalNetOut.push(info.totalNetOutputBytes / (1024));
                Data.expiredKeys.push(info.expiredKeys);
                Data.evictedKeys.push(info.evictedKeys);
                if (i == holdData.length - 1) {
                    $('#svr_memory').html(info.usedMemoryHuman);
                    $('#svr_memory_peak').html(info.usedMemoryPeakHuman);
                    $('#svr_cnn_clients').html(info.connectedClients);
                    $('#svr_up_days').html(info.uptimeInDays);
                    $('#svr_tcp_port').html(info.tcpPort);
                    $('#svr_os').html(info.os);
                    $('#svr_version').html(info.redisVersion);
                    $('#svr_role').html(info.role);
                }
            }

            switch (type) {
                case "memory":
                    option = memoryChart.getOption();
                    option.xAxis[0].data = Data.dateList;
                    option.series[0].data = Data.memoryPeak;
                    option.series[1].data = Data.memoryCur;
                    memoryChart.setOption(option);
                    break;
                case "stat":
                    option = statChart.getOption();
                    option.xAxis[0].data = Data.dateList;
                    option.series[0].data = Data.cmdProcessed;
                    option.series[1].data = Data.totalCnnRev;
                    option.series[2].data = Data.expiredKeys;
                    option.series[3].data = Data.evictedKeys;
                    statChart.setOption(option);
                    break;
                case "network":
                    option = networkChart.getOption();
                    option.xAxis[0].data = Data.dateList;
                    option.series[0].data = Data.totalNetIn;
                    option.series[1].data = Data.totalNetOut;
                    networkChart.setOption(option);
                    break;
            }
        }

        function fetchServerData(eChart, timeSpan, callback) {
            $.getJSON(Config.url, {timeSpan: timeSpan}, function (data) {
                if (!data) {
                    alert('no data');
                    return;
                }
                clearData();
                holdData = data;
                callback(eChart);
            });
        }

        $(function () {
            var progress = $('.ui.progress').progress({
                percent: 0
            });
            $('.ui.menu a.item')
                    .on('click', function () {
                        $(this)
                                .addClass('active')
                                .siblings()
                                .removeClass('active')
                        ;
                        timeSpan = $(this).attr('data-span');
                        progress.progress('reset');
                        $('.ui.basic.modal').modal('show');
                        var innerTimer = setInterval(function () {
                            progress.progress('increment', 5);
                        }, 250);
                        setTimeout(function () {
                            clearTimeout(innerTimer);
                            $('.ui.basic.modal').modal('hide');
                        }, 5500);
                    });



        });
    </script>
</head>
<body>
<div class="ui inverted menu">
    <div class="header item">
        Redis Monitor
    </div>
    <div class="right menu">
        <div class="item">
            <a href="https://github.com/yamorn/redis-monitor">GitHub</a>
        </div>
    </div>
</div>
<div class="ui four statistics">


    <div class="ui green statistic">
        <div id="svr_memory" class="value">
        </div>
        <div class="label">
            Memory
        </div>
    </div>

    <div class="ui red statistic">
        <div id="svr_memory_peak" class="value">
        </div>
        <div class="label">
            Memory Peak
        </div>
    </div>

    <div class="ui blue statistic">
        <div id="svr_cnn_clients" class="value">
        </div>
        <div class="label">
            Connected Clients
        </div>
    </div>

    <div class="ui orange statistic">
        <div id="svr_up_days" class="value">
        </div>
        <div class="label">
            Server Up Days
        </div>
    </div>

</div>
<div style="height:50px"></div>
<div class="ui mini four statistics">
    <div class="ui mini statistic">
        <div id="svr_tcp_port" class="value">
        </div>
        <div class="label">
            TCP Port
        </div>
    </div>

    <div class="ui mini statistic">
        <div id="svr_os" class="value">
        </div>
        <div class="label">
            OS
        </div>
    </div>
    <div class="ui mini statistic">
        <div id="svr_version" class="value">
        </div>
        <div class="label">
            Redis Version
        </div>
    </div>
    <div class="ui mini statistic">
        <div id="svr_role" class="value">
        </div>
        <div class="label">
            Role
        </div>
    </div>
</div>
<div style="height:50px"></div>
<div id="timeSpan" class="ui blue seven item menu">
    <a href="javascript:void(0);" data-span="REAL_TIME" class="active item">
        Real Time
    </a>
    <a href="javascript:void(0);" data-span="ONE_HOUR" class="item">
        One Hour
    </a>
    <a href="javascript:void(0);" data-span="TWO_HOUR" class="item">
        Two Hour
    </a>
    <a href="javascript:void(0);" data-span="THREE_HOUR" class="item">
        Three Hour
    </a>
    <a href="javascript:void(0);" data-span="FOUR_HOUR" class="item">
        Four Hour
    </a>
    <a href="javascript:void(0);" data-span="TODAY" class="item">
        Today
    </a>
    <a href="javascript:void(0);" data-span="YESTERDAY" class="item">
        Yesterday
    </a>
</div>
<div id="memory" style="height:400px;"></div>
<div id="stat" style="height:400px"></div>
<div id="network" style="height:400px"></div>

<div class="ui basic modal">
    <div class="header">
        Switch Modal
    </div>
    <div class="content">
        <div class="ui progress success">
            <div class="bar">
                <div class="progress"></div>
            </div>
            <div class="label">data load...</div>
        </div>
    </div>
</div>
</body>
</html>
