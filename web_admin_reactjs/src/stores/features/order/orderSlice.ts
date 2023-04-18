import { PayloadAction, createAsyncThunk, createSlice } from '@reduxjs/toolkit'
import IOrder from '../../../services/order/types/IOrder';

export interface IOrderState {
    data: Array<IOrder>,
}

const initialState = (): IOrderState => {
    return {
        data: [],
    }
}


export const orderSlice = createSlice({
    name: 'order',
    initialState,
    reducers: {
        addOrder: (state, action: PayloadAction<{
            data: IOrder,
            pageSize: number | undefined,
        }>) => {
            if (state.data.length === action.payload.pageSize) {
                state.data.pop();
            }
            state.data = [action.payload.data, ...state.data]
        },
        updateOrder: (state, { payload }) => {
            state.data = state.data.map((item) => {
                if (item.id === payload.id) {
                    return payload;
                }
                return item;
            });
        },
        deleteOrderById: (state, { payload }: PayloadAction<{
            id: string | number,
        }>) => {
            state.data = state.data.filter((item) => item.id !== payload.id);
        },
        setOrderData: (state, { payload }) => {
            state.data = payload;
        }
    },
})

// Action creators are generated for each case reducer function
export const { addOrder, updateOrder, deleteOrderById, setOrderData } = orderSlice.actions

export default orderSlice.reducer