var stats = {
    type: "GROUP",
name: "Global Information",
path: "",
pathFormatted: "group_missing-name-b06d1",
stats: {
    "name": "Global Information",
    "numberOfRequests": {
        "total": "605",
        "ok": "605",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "75",
        "ok": "75",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "30122",
        "ok": "30122",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "7774",
        "ok": "7774",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "13058",
        "ok": "13058",
        "ko": "-"
    },
    "percentiles1": {
        "total": "89",
        "ok": "89",
        "ko": "-"
    },
    "percentiles2": {
        "total": "30009",
        "ok": "30009",
        "ko": "-"
    },
    "percentiles3": {
        "total": "30023",
        "ok": "30023",
        "ko": "-"
    },
    "percentiles4": {
        "total": "30050",
        "ok": "30050",
        "ko": "-"
    },
    "group1": {
    "name": "t < 800 ms",
    "count": 450,
    "percentage": 74
},
    "group2": {
    "name": "800 ms < t < 1200 ms",
    "count": 0,
    "percentage": 0
},
    "group3": {
    "name": "t > 1200 ms",
    "count": 155,
    "percentage": 26
},
    "group4": {
    "name": "failed",
    "count": 0,
    "percentage": 0
},
    "meanNumberOfRequestsPerSecond": {
        "total": "13.444",
        "ok": "13.444",
        "ko": "-"
    }
},
contents: {
"req_request-0-684d2": {
        type: "REQUEST",
        name: "request_0",
path: "request_0",
pathFormatted: "req_request-0-684d2",
stats: {
    "name": "request_0",
    "numberOfRequests": {
        "total": "605",
        "ok": "605",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "75",
        "ok": "75",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "30122",
        "ok": "30122",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "7774",
        "ok": "7774",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "13058",
        "ok": "13058",
        "ko": "-"
    },
    "percentiles1": {
        "total": "89",
        "ok": "89",
        "ko": "-"
    },
    "percentiles2": {
        "total": "30009",
        "ok": "30009",
        "ko": "-"
    },
    "percentiles3": {
        "total": "30023",
        "ok": "30023",
        "ko": "-"
    },
    "percentiles4": {
        "total": "30050",
        "ok": "30050",
        "ko": "-"
    },
    "group1": {
    "name": "t < 800 ms",
    "count": 450,
    "percentage": 74
},
    "group2": {
    "name": "800 ms < t < 1200 ms",
    "count": 0,
    "percentage": 0
},
    "group3": {
    "name": "t > 1200 ms",
    "count": 155,
    "percentage": 26
},
    "group4": {
    "name": "failed",
    "count": 0,
    "percentage": 0
},
    "meanNumberOfRequestsPerSecond": {
        "total": "13.444",
        "ok": "13.444",
        "ko": "-"
    }
}
    }
}

}

function fillStats(stat){
    $("#numberOfRequests").append(stat.numberOfRequests.total);
    $("#numberOfRequestsOK").append(stat.numberOfRequests.ok);
    $("#numberOfRequestsKO").append(stat.numberOfRequests.ko);

    $("#minResponseTime").append(stat.minResponseTime.total);
    $("#minResponseTimeOK").append(stat.minResponseTime.ok);
    $("#minResponseTimeKO").append(stat.minResponseTime.ko);

    $("#maxResponseTime").append(stat.maxResponseTime.total);
    $("#maxResponseTimeOK").append(stat.maxResponseTime.ok);
    $("#maxResponseTimeKO").append(stat.maxResponseTime.ko);

    $("#meanResponseTime").append(stat.meanResponseTime.total);
    $("#meanResponseTimeOK").append(stat.meanResponseTime.ok);
    $("#meanResponseTimeKO").append(stat.meanResponseTime.ko);

    $("#standardDeviation").append(stat.standardDeviation.total);
    $("#standardDeviationOK").append(stat.standardDeviation.ok);
    $("#standardDeviationKO").append(stat.standardDeviation.ko);

    $("#percentiles1").append(stat.percentiles1.total);
    $("#percentiles1OK").append(stat.percentiles1.ok);
    $("#percentiles1KO").append(stat.percentiles1.ko);

    $("#percentiles2").append(stat.percentiles2.total);
    $("#percentiles2OK").append(stat.percentiles2.ok);
    $("#percentiles2KO").append(stat.percentiles2.ko);

    $("#percentiles3").append(stat.percentiles3.total);
    $("#percentiles3OK").append(stat.percentiles3.ok);
    $("#percentiles3KO").append(stat.percentiles3.ko);

    $("#percentiles4").append(stat.percentiles4.total);
    $("#percentiles4OK").append(stat.percentiles4.ok);
    $("#percentiles4KO").append(stat.percentiles4.ko);

    $("#meanNumberOfRequestsPerSecond").append(stat.meanNumberOfRequestsPerSecond.total);
    $("#meanNumberOfRequestsPerSecondOK").append(stat.meanNumberOfRequestsPerSecond.ok);
    $("#meanNumberOfRequestsPerSecondKO").append(stat.meanNumberOfRequestsPerSecond.ko);
}
