import { Message } from 'element-ui';

export function buildMsg(msg,type) {
    Message({
        message: msg,
        type: type || 'error',
        duration: 3 * 1000,
    });
}