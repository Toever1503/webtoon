import { Breadcrumb, Button } from 'antd'
import React, { useEffect } from 'react'
import { useSelector, useDispatch } from 'react-redux'
import { RootState } from '../../stores'
import { decrement, increment } from '../../stores/features/counter/counterSlice'

export default function CounterPage() {
    const counter = useSelector((state: RootState) => state.counter)
    const count = useSelector((state: RootState) => state.counter.value)
    const dispatch = useDispatch()

    const counterStatus = () => {
        console.log('counter', counter)
    }

    useEffect(() => {
        console.log('counter val changed: ', counter);
    }, [count])

    return (
        <div>
            <Breadcrumb style={{ margin: '16px 0' }}>
                <Breadcrumb.Item>User</Breadcrumb.Item>
                <Breadcrumb.Item>Bill</Breadcrumb.Item>
            </Breadcrumb>
            <div className='text-red-500' style={{ padding: 24, minHeight: 360, }}>
                Bill is a cat.
            </div>
            
            <div>
                <button
                    aria-label="Increment value"
                    onClick={() => dispatch(increment())}
                >
                    Increment
                </button>
                <span>{count}</span>
                <button
                    aria-label="Decrement value"
                    onClick={() => dispatch(decrement())}
                >
                    Decrement
                </button>
            </div>
            <Button type='primary' onClick={counterStatus} >
                Counter Status
            </Button>
        </div>
    )
}