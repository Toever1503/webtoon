import React from 'react'
import ReactDOM from 'react-dom/client'
import {
  RouterProvider,
} from "react-router-dom";
import router from './routes';
import { Provider } from 'react-redux'
import './index.css'
import { store } from './stores';
import 'antd/dist/reset.css';
import './plugins/i18n';
import { registerLicense } from '@syncfusion/ej2-base';
import { ConfigProvider } from 'antd';
import viVN from 'antd/lib/locale/vi_VN';

registerLicense('ORg4AjUWIQA/Gnt2VVhkQlFac1xJXGFWfVJpTGpQdk5xdV9DaVZUTWY/P1ZhSXxQdkZhXH5edX1QRGFcVkM=');


ReactDOM.createRoot(document.getElementById('root') as HTMLElement).render(
  <ConfigProvider locale={viVN}>
    <Provider store={store}>
      <RouterProvider router={router} />
    </Provider>
  </ConfigProvider>

)
