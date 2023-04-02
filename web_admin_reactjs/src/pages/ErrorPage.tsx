import React, { useEffect } from 'react';
import { Button, Result } from 'antd';
import { useNavigate, useNavigation, useSearchParams } from 'react-router-dom';

interface ErrorPageProps {
}

const ErrorPage: React.FC<ErrorPageProps> = (props: ErrorPageProps) => {
    const navigate = useNavigate();
    let [searchParams] = useSearchParams();

    const handleBack = () => {
        console.log('handle back: ', searchParams.get('to'));
        if (searchParams.get('to'))
            navigate(searchParams.get('to') || '/');
        else navigate(-1);
    };

    useEffect(() => {
    }, []);

    return <>
        < Result
            status="500"
            title="500"
            subTitle="Sorry, something went wrong."
            extra={<Button type="primary" onClick={handleBack}>Quay lại</Button>}
        />
    </>
}

export default ErrorPage;