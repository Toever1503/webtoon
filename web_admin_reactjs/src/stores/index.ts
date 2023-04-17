import { configureStore } from '@reduxjs/toolkit'
import counterSlice from './features/counter/counterSlice'
import notificationSlice from './features/notification/notificationSlice';
import genreSlice from './features/manga/genreSlice';
import tagSlice from './features/manga/tagSlice';
import authorSlice from './features/manga/authorSlice';
import userSlice from './features/user/userSlice';
import subscriptionPackSlice from './features/subscription-pack/subscriptionPackSlice';

export const store = configureStore({
  reducer: {
    counter: counterSlice,
    genre: genreSlice,
    author: authorSlice,
    tag: tagSlice,
    user: userSlice,
    subscriptionPack: subscriptionPackSlice,
    notification: notificationSlice
  },
})

// Infer the `RootState` and `AppDispatch` types from the store itself
export type RootState = ReturnType<typeof store.getState>
// Inferred type: {posts: PostsState, comments: CommentsState, users: UsersState}
export type AppDispatch = typeof store.dispatch

