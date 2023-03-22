import { notification } from "antd";
import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../stores";
import { hideNofification, NotificationType, showNofification } from "../stores/features/notification/notificationSlice";



const NotificationComponent: React.FC = () => {
    const notificationData = useSelector((state: RootState) => state.notification);
    const dispatch = useDispatch();

    const [api, contextHolder] = notification.useNotification();
    const openNotificationWithIcon = (type: NotificationType, message: string) => {
        api[type]({ message });
    };

    useEffect(() => {
        if (notificationData.isShow) {
            openNotificationWithIcon(notificationData.type, notificationData.message);
            dispatch(hideNofification());
        }
    }, [notificationData]);
    return (<>
        {contextHolder}
    </>)
};

export default NotificationComponent;