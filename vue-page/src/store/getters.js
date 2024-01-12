const getters = {
    userInfo: state => state.user.userInfo,
    recentlyMessageMap: state => state.message.recentlyMessageMap,
    recentlyMessageActivate: state => state.message.recentlyMessageActivate,
    messageMap: state => state.message.messageMap,
};
export default getters;