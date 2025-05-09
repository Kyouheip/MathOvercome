// /mypage/[userId]/CreateSession.js
'use client'
import {useRouter} from 'next/navigation';
import {useState} from 'react';

export default function CreateSession(){
    const [includeIntegers,setIncludeIntegers] = useState(false);
    const router = useRouter();

    const startTest = async () => {
        const res = await fetch(
            `${process.env.NEXT_PUBLIC_API_URL}/session/test?includeIntegers=${includeIntegers}`,
            {
            method: 'post',
            credentials: 'include',
            }

        );

        if (!res.ok) {
            alert('エラー発生: ' + res.status);
            return;
        }
        
        const session = await res.json();
        console.log('APIレスポンス:', session);

        const idx = 0;
        router.push(`/session/${session.id}/problems/${idx}`);
    };

    return(
        <div>
            <h2>新規テスト開始</h2>
            <label>
                <input type="checkbox"
                    checked={includeIntegers}
                    onChange={e => setIncludeIntegers(e.target.checked)}/>
                    整数分野も問題に含める
            </label>
            <button onClick={startTest}>テスト開始</button>
        </div>
    );
}