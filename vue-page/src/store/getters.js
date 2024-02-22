const getters = {
    userInfo: state => state.user.userInfo,
    navActivate: state => state.common.navActivate,
    recentlyMessageMap: state => state.message.recentlyMessageMap,
    messageUnReadMap: state => state.message.messageUnReadMap,
    recentlyMessageActivate: state => state.message.recentlyMessageActivate,
    messageMap: state => state.message.messageMap,
    contactsActivate: state => state.contacts.contactsActivate,
};
export default getters;