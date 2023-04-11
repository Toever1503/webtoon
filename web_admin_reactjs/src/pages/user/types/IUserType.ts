
export default interface IUserType {
    id?: number | string;
    fullName: string;
    username: string;
    email: string;
    password?: string;
    phone?: string;
    sex: IUserSex;
    avatar?: string;
    status: IUserStatus;
    accountType: 'FB' | 'GOOGLE' | 'NORMAL';
    authorities: {
        id: number | string;
        authorityName: string;
    }[]
}

export type IUserStatus = 'NOT_ENABLED' | 'DEACTIVED' | 'ACTIVED';
export type IUserSex = 'MALE' | 'FEMALE';
export const USER_STATUS_LIST: IUserStatus[] = ['DEACTIVED' , 'ACTIVED'];