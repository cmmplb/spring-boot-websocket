/**
 * 转换日期格式
 * @param date 待转换的日期
 * @param type 转换类型
 * @returns {string}
 * 1-所有:小于60秒显示刚刚、(不显示今天文字)HH:mm、昨天 HH:mm、星期* HH:mm、yyyy年MM月dd日 HH:mm
 * 2-(不显示今天文字)HH:mm、昨天 HH:mm、星期* HH:mm、yyyy年MM月dd日 HH:mm
 * 3-(不显示今天文字)HH:mm、yyyy/MM/dd
 * 其他或为空显示当前格式化的后时间:yyyy年MM月dd日 HH:mm
 */
export function dateFormat(date, type) {
    const dateObj = getDateObj(date);
    const currentObj = getCurrentDateObj();
    const dayPerMonthAddTime = getDayPerMonth(dateObj.year);
    let timeStr = "";

    function format() {
        if (dateObj.year === currentObj.year && dateObj.month === currentObj.month && dateObj.date === currentObj.date) {
            // (不显示今天文字)HH:mm
            timeStr = dateObj.hours + ":" + dateObj.minutes;
        } else if ((dateObj.year === currentObj.year && dateObj.month === currentObj.month && dateObj.date === currentObj.date - 1)
            || (dateObj.year === currentObj.year && currentObj.month - dateObj.month === 1 && dayPerMonthAddTime[dateObj.month] === dateObj.date && currentObj.date === 1)
            || (currentObj.year - dateObj.year === 1 && dateObj.month === 12 && dateObj.date === 31 && currentObj.month === 1 && currentObj.date === 1)) {
            // 昨天 HH:mm
            timeStr = "昨天 " + dateObj.hours + ":" + dateObj.minutes + " ";
        } else if ((dateObj.year === currentObj.year && dateObj.month === currentObj.month && currentObj.date - dateObj.date < 7)
            || (dateObj.year === currentObj.year && currentObj.month - dateObj.month === 1 && dayPerMonthAddTime[dateObj.month] - dateObj.date + currentObj.date < 7
                || (currentObj.year - dateObj.year === 1 && dateObj.month === 12 && currentObj.month === 1 && 31 - dateObj.date + currentObj.date < 7))) {
            // 星期* HH:mm
            timeStr = ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"][dateObj.day] + " " + dateObj.hours + ":" + dateObj.minutes;
        } else {
            // yyyy年MM月dd日 HH:mm
            timeStr = dateObj.year + "年" + dateObj.month + "月" + dateObj.date + "日 " + dateObj.hours + ":" + dateObj.minutes;
        }
        return timeStr;
    }

    if (type === 1) {
        if ((new Date() - new Date(Date.parse(new Date(date) + ""))) / 1000 <= 60) {
            return "刚刚";
        } else {
            return format();
        }
    }
    if (type === 2) {
        return format();
    }
    if (type === 3) {
        if (dateObj.year === currentObj.year && dateObj.month === currentObj.month && dateObj.date === currentObj.date) {
            // (不显示今天文字)HH:mm
            return dateObj.hours + ":" + dateObj.minutes;
        } else {
            return dateObj.year + "/" + dateObj.month + "/" + dateObj.date;
        }
    }
    return dateObj.year + "年" + dateObj.month + "月" + dateObj.date + "日 " + dateObj.hours + ":" + dateObj.minutes;
}

/**
 * 获取当前格式化的后时间
 * @returns {string} yyyy-MM-dd HH:mm:ss
 */
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

function getDateObj(date) {
    const dateTimestamp = new Date(Date.parse(new Date(date) + ""));
    const dateObj = {
        "year": dateTimestamp.getFullYear(),
        "month": parseInt(dateTimestamp.getMonth() + "") + 1,
        "date": parseInt(dateTimestamp.getDate() + ""),
        "day": dateTimestamp.getDay(),
        "hours": dateTimestamp.getHours(),
        "minutes": dateTimestamp.getMinutes(),
        "seconds": dateTimestamp.getSeconds()
    };
    if (dateTimestamp.getMinutes() < 10) {
        // 分钟数值不足2位补充前置0
        dateObj.minutes = "0" + dateObj.minutes;
    }
    return dateObj;
}

function getCurrentDateObj() {
    const currentTimestamp = new Date();
    return {
        "year": currentTimestamp.getFullYear(),
        "month": parseInt(currentTimestamp.getMonth() + "") + 1,
        "date": parseInt(currentTimestamp.getDate() + ""),
        "day": currentTimestamp.getDay(),
        "hours": currentTimestamp.getHours(),
        "minutes": currentTimestamp.getMinutes(),
        "seconds": currentTimestamp.getSeconds()
    };
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
