export function dateFormat(date) {
    const timestamp = Date.parse(new Date(date) + "");
    const dateTimestamp = new Date(timestamp);
    const currentTimestamp = new Date();
    const dateObj = {
        "year": dateTimestamp.getFullYear(),
        "month": parseInt(dateTimestamp.getMonth() + "") + 1,
        "date": parseInt(dateTimestamp.getDate() + ""),
        "day": dateTimestamp.getDay(),
        "hours": dateTimestamp.getHours(),
        "minutes": dateTimestamp.getMinutes(),
        "seconds": dateTimestamp.getSeconds()
    };
    const currentObj = {
        "year": currentTimestamp.getFullYear(),
        "month": parseInt(currentTimestamp.getMonth() + "") + 1,
        "date": parseInt(currentTimestamp.getDate() + ""),
        "day": currentTimestamp.getDay(),
        "hours": currentTimestamp.getHours(),
        "minutes": currentTimestamp.getMinutes(),
        "seconds": currentTimestamp.getSeconds()
    };
    if (dateTimestamp.getMinutes() < 10) {
        // 分钟数值不足2位补充前置0
        dateObj.minutes = "0" + dateObj.minutes;
    }
    let timeStr = "";
    const dayPerMonthAddTime = getDayPerMonth(dateObj.year);
    const week = ["周日", "周一", "周二", "周三", "周四", "周五", "周六"];
    // 当前时间和传入日期小于60秒显示刚刚
    if ((currentTimestamp - dateTimestamp) / 1000 <= 60) {
        timeStr = "刚刚";
    } else if (dateObj.year === currentObj.year && dateObj.month === currentObj.month && dateObj.date === currentObj.date) {
        // 今天
        timeStr += dateObj.hours + ":" + dateObj.minutes;
    } else if ((dateObj.year === currentObj.year && dateObj.month === currentObj.month && dateObj.date === currentObj.date - 1)
        || (dateObj.year === currentObj.year && currentObj.month - dateObj.month === 1 && dayPerMonthAddTime[dateObj.month] === dateObj.date && currentObj.date === 1)
        || (currentObj.year - dateObj.year === 1 && dateObj.month === 12 && dateObj.date === 31 && currentObj.month === 1 && currentObj.date === 1)) {
        // 昨天
        timeStr += "昨天 " + dateObj.hours + ":" + dateObj.minutes + " ";
    } else if ((dateObj.year === currentObj.year && dateObj.month === currentObj.month && currentObj.date - dateObj.date < 7)
        || (dateObj.year === currentObj.year && currentObj.month - dateObj.month === 1 && dayPerMonthAddTime[dateObj.month] - dateObj.date + currentObj.date < 7
            || (currentObj.year - dateObj.year === 1 && dateObj.month === 12 && currentObj.month === 1 && 31 - dateObj.date + currentObj.date < 7))) {
        // 上周
        timeStr += week[dateObj.day] + " " + dateObj.hours + ":" + dateObj.minutes;
    } else {
        // 年月日时分
        timeStr += dateObj.year + "年" + dateObj.month + "月" + dateObj.date + "日 " + dateObj.hours + ":" + dateObj.minutes;
    }
    return timeStr;
}

function getDayPerMonth(year) {
    const arr = [];
    arr[1] = 31;
    arr[3] = 31;
    arr[4] = 30;
    arr[5] = 31;
    arr[6] = 30;
    arr[7] = 31;
    arr[8] = 31;
    arr[9] = 30;
    arr[10] = 31;
    arr[11] = 30;
    arr[12] = 31;
    if ((year % 4 === 0 && year % 100 !== 0) || (year % 400 === 0)) {
        arr[2] = 29;
    } else {
        arr[2] = 28;
    }
    return arr;
}

export function currentDateTime() {
    const date = new Date();
    const year = date.getFullYear();
    const month = ("0" + (date.getMonth() + 1)).slice(-2);
    const day = ("0" + date.getDate()).slice(-2);
    const hours = ("0" + date.getHours()).slice(-2);
    const minutes = ("0" + date.getMinutes()).slice(-2);
    const seconds = ("0" + date.getSeconds()).slice(-2);
    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}