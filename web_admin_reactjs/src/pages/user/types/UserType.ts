
export default interface IUserType {
    id: number | string;
    fullName: string;
    username: string;
    email: string;
    password?: string;
    phone?: string;
    sex: 'MALE' | 'FEMALE' | 'OTHER';
    avatar: string;
    status: IUserStatus;
    accountType: 'FB' | 'GOOGLE' | 'NORMAL';
}

export type IUserStatus = 'NOT_ENABLED' | 'ENABLED' | 'NOT_ACTIVE' | 'ACTIVED';
export const USER_STATUS_LIST: IUserStatus[] = ['NOT_ENABLED', 'ENABLED', 'NOT_ACTIVE', 'ACTIVED'];