import { PayloadAction, createAsyncThunk, createSlice } from '@reduxjs/toolkit'
import ISubscriptionPack from '../../../services/subscription_pack/types/ISubscriptionPack';

export interface SubscriptionPackState {
    data: Array<ISubscriptionPack>,
}

const initialState = (): SubscriptionPackState => {
    return {
        data: [],

    }
}




export const subscriptionPackSlice = createSlice({
    name: 'subscriptionPack',
    initialState,
    reducers: {
        addSubscriptionPack: (state, action: PayloadAction<{
            data: ISubscriptionPack,
            pageSize: number | undefined,
        }>) => {
            if (state.data.length === action.payload.pageSize) {
                state.data.pop();
            }
            state.data = [action.payload.data, ...state.data]
        },
        updateSubscriptionPack: (state, { payload }) => {
            state.data = state.data.map((item) => {
                if (item.id === payload.id) {
                    return payload;
                }
                return item;
            });
        },
        deleteSubscriptionPackById: (state, { payload }: PayloadAction<{
            id: string | number,
        }>) => {
            state.data = state.data.filter((item) => item.id !== payload.id);
        },
        setSubscriptionPackData: (state, { payload }) => {
            state.data = payload;
        }
    },
})

// Action creators are generated for each case reducer function
export const { addSubscriptionPack, updateSubscriptionPack, deleteSubscriptionPackById, setSubscriptionPackData } = subscriptionPackSlice.actions

export default subscriptionPackSlice.reducer