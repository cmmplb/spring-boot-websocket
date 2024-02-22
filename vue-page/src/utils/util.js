import CryptoJS from "crypto-js";

export const uuidTrim = () => {
    return uuid().replaceAll("-", "");
};

/**
 * generateUUID 生成UUID
 * @returns {string} 返回字符串
 */
export const uuid = () => {
    let d = new Date().getTime();
    return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, function (c) {
        var r = (d + Math.random() * 16) % 16 | 0;
        d = Math.floor(d / 16);
        return (c === "x" ? r : (r & 0x7 | 0x8)).toString(16);
    });
};

/**
 * 生成随机len位数字
 * @param len 长度
 * @param date 是否使用日期数
 * @returns {string}
 */
export const randomLenNum = (len, date) => {
    let random = "";
    random = Math.ceil(Math.random() * 100000000000000)
        .toString()
        .substr(0, len || 4);
    if (date) {
        random = random + Date.now();
    }
    return random;
};

/**
 * 动态插入css
 */
export const loadStyle = url => {
    const link = document.createElement("link");
    link.type = "text/css";
    link.rel = "stylesheet";
    link.href = url;
    const head = document.getElementsByTagName("head")[0];
    head.appendChild(link);
};

/**
 * 判断路由是否相等
 */
export const diff = (obj1, obj2) => {
    delete obj1.close;
    var o1 = obj1 instanceof Object;
    var o2 = obj2 instanceof Object;
    if (!o1 || !o2) {
        /*  判断不是对象  */
        return obj1 === obj2;
    }

    if (Object.keys(obj1).length !== Object.keys(obj2).length) {
        return false;
        // Object.keys() 返回一个由对象的自身可枚举属性(key值)组成的数组,例如：数组返回下表：let arr = ["a", "b", "c"];console.log(Object.keys(arr))->0,1,2;
    }

    for (var attr in obj1) {
        var t1 = obj1[attr] instanceof Object;
        var t2 = obj2[attr] instanceof Object;
        if (t1 && t2) {
            return diff(obj1[attr], obj2[attr]);
        } else if (obj1[attr] !== obj2[attr]) {
            return false;
        }
    }
    return true;
};

/**
 * 构造树型结构数据
 * @param {*} data 数据源
 * @param {*} id id字段 默认 'id'
 * @param {*} parentId 父节点字段 默认 'parentId'
 * @param {*} children 孩子节点字段 默认 'children'
 * @param {*} rootId 根Id 默认 0
 */
export function handleTree(data, id, parentId, children, rootId) {
    id = id || "id";
    parentId = parentId || "parentId";
    children = children || "children";
    rootId = rootId || Math.min.apply(Math, data.map(item => {
        return item[parentId];
    })) || 0;
    //对源数据深度克隆
    const cloneData = JSON.parse(JSON.stringify(data));
    //循环所有项
    const treeData = cloneData.filter(father => {
        let branchArr = cloneData.filter(child => {
            //返回每一项的子级数组
            return father[id] === child[parentId];
        });
        branchArr.length > 0 ? father.children = branchArr : "";
        //返回第一层
        return father[parentId] === rootId;
    });
    return treeData !== "" ? treeData : data;
}

// 排除相同元素name
export function resArr(arr1, arr2) {
    return arr1.filter((v) => arr2.every((val) => val.name !== v.name));
}

/**
 * @word 要加密的内容
 * @keyWord String  服务器随机返回的关键字
 *  */
export function aesEncrypt(word, keyWord = "XwKsGlMcdPMEhR1B") {
    var key = CryptoJS.enc.Utf8.parse(keyWord);
    var srcs = CryptoJS.enc.Utf8.parse(word);
    var encrypted = CryptoJS.AES.encrypt(srcs, key, {mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.Pkcs7});
    return encrypted.toString();
}


export function resetSize(vm) {
    var img_width, img_height, bar_width, bar_height;	// 图片的宽度、高度，移动条的宽度、高度

    var parentWidth = vm.$el.parentNode.offsetWidth || window.offsetWidth;
    var parentHeight = vm.$el.parentNode.offsetHeight || window.offsetHeight;

    if (vm.imgSize.width.indexOf("%") != -1) {
        img_width = parseInt(this.imgSize.width) / 100 * parentWidth + "px";
    } else {
        img_width = this.imgSize.width;
    }

    if (vm.imgSize.height.indexOf("%") != -1) {
        img_height = parseInt(this.imgSize.height) / 100 * parentHeight + "px";
    } else {
        img_height = this.imgSize.height;
    }

    if (vm.barSize.width.indexOf("%") != -1) {
        bar_width = parseInt(this.barSize.width) / 100 * parentWidth + "px";
    } else {
        bar_width = this.barSize.width;
    }

    if (vm.barSize.height.indexOf("%") != -1) {
        bar_height = parseInt(this.barSize.height) / 100 * parentHeight + "px";
    } else {
        bar_height = this.barSize.height;
    }

    return {imgWidth: img_width, imgHeight: img_height, barWidth: bar_width, barHeight: bar_height};
}

export const _code_chars = [1, 2, 3, 4, 5, 6, 7, 8, 9, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"];
export const _code_color1 = ["#fffff0", "#f0ffff", "#f0fff0", "#fff0f0"];
export const _code_color2 = ["#FF0033", "#006699", "#993366", "#FF9900", "#66CC66", "#FF33CC"];