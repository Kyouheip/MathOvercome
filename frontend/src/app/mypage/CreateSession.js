// /component of mypage
'use client'
import {useRouter} from 'next/navigation';
import {useState} from 'react';

export default function CreateSession(){
    const [includeIntegers,setIncludeIntegers] = useState(false);
    const router = useRouter();

    const startTest = async () => {
        const res = await fetch(
            `${process.env.NEXT_PUBLIC_API_BASE_URL}/session/test?includeIntegers=${includeIntegers}`,
            {method: 'post'}
        );

        if (!res.ok) {
            alert('エラー発生: ' + res.status);
            return;
        }
        
        const session = await res.json();
        console.log('APIレスポンス:', session);

        router.push(`/session/${session.id}/problems`);
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