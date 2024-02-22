export const emojiData = [
    {
        "src": require("@/assets/images/emoji/1.png"),
        "name": "微笑",
        "regName": "[微笑]"
    },
    {
        "src": require("@/assets/images/emoji/2.png"),
        "name": "爱慕",
        "regName": "[爱慕]"
    },
    {
        "src": require("@/assets/images/emoji/3.png"),
        "name": "惊呆",
        "regName": "[惊呆]"
    },
    {
        "src": require("@/assets/images/emoji/4.png"),
        "name": "酷拽",
        "regName": "[酷拽]"
    },
    {
        "src": require("@/assets/images/emoji/5.png"),
        "name": "抠鼻",
        "regName": "[抠鼻]"
    },
    {
        "src": require("@/assets/images/emoji/6.png"),
        "name": "流泪",
        "regName": "[流泪]"
    },
    {
        "src": require("@/assets/images/emoji/7.png"),
        "name": "捂脸",
        "regName": "[捂脸]"
    },
    {
        "src": require("@/assets/images/emoji/8.png"),
        "name": "发怒",
        "regName": "[发怒]"
    },
    {
        "src": require("@/assets/images/emoji/9.png"),
        "name": "呲牙",
        "regName": "[呲牙]"
    },
    {
        "src": require("@/assets/images/emoji/10.png"),
        "name": "尬笑",
        "regName": "[尬笑]"
    },
    {
        "src": require("@/assets/images/emoji/11.png"),
        "name": "害羞",
        "regName": "[害羞]"
    },
    {
        "src": require("@/assets/images/emoji/12.png"),
        "name": "可爱",
        "regName": "[可爱]"
    },
    {
        "src": require("@/assets/images/emoji/13.png"),
        "name": "舔屏",
        "regName": "[舔屏]"
    },
    {
        "src": require("@/assets/images/emoji/14.png"),
        "name": "看",
        "regName": "[看]"
    }
];

/**
 * 解析富文本内容
 * @param {Object} html 富文本内容
 */
export function parseToText(html) {
    // 1、对img表情进行解析，解析出表情对应的name
    let reg = /<img[\S\s]*?>/g;
    let imgList = html.match(reg);
    let length = imgList?.length || 0;
    html = parseEmojiName(html, imgList);
    // 3、解析富文本自带标签p
    html = parseDefaultTab(html);
    // console.log(html);
    let html1 = html.replace(new RegExp(/\[[\u4e00-\u9fa5]+\]/, "g"), "");
    return {
        text: html,
        length: html1.length + length
    };
}

export function parseEmojiName(html, emojiList) {
    try {
        if (!emojiList) {
            return html;
        }
        console.log("emojiList:", emojiList);
        for (const imgStr of emojiList) {
            let name = imgStr.match(/name=[\S\s]*\]/)[0];
            name = name.replace("name=\"", "");
            console.log("name:", name);
            html = html.replace(imgStr, name);
        }
    } catch (err) {
        console.log("error:", err);
    }
    return html;
}

/**
 * 解析富文本编辑器默认标签，p标签
 * @param {Object} html
 */
export function parseDefaultTab(html) {
    let reg = new RegExp("<div>", "g");
    let reg1 = new RegExp("</div>", "g");
    let reg2 = new RegExp("<br?>", "g");
    let reg3 = new RegExp("^\\s+|\\s+$", "g");
    // html = html.replace(reg, "\n");
    html = html.replace(reg, "");
    html = html.replace(reg1, "");
    html = html.replace(reg2, "\n");
    // 去除首尾多余换行，^代表开头，$代表结尾，\s 代表：匹配一个空白字符，包括\n,\r,\f,\t,\v等，g：globle进行全局匹配
    // html = html.replace(/^\s+|\s+$/g, "");
    html = html.replace(reg3, "");
    return html;
}

// 表情包回显
export function echoEmojis(str) {
    emojiData.forEach(ele => {
        str = str.replaceAll(ele.regName, `<img src='${ele.src}' class='in-img' alt=''>`);
    });
    return str;
}