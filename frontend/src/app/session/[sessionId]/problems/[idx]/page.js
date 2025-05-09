//session/[sessionId]/problems/[idx]/page.js
import QuestionForm from './QuestionForm';

export default async function ProblemPage({params}){
    const {sessionId,idx}=params;
    const res = await fetch(
        `${process.env.NEXT_PUBLIC_API_URL}/session/${sessionId}/problems/${idx}`,
        {
        cache:'no-store',
        credentials: 'include',
        }
    );

    if(!res.ok){
        return <p>エラー: 問題{idx}が取得できませんでした。(${res.status})</p>;
    }

    const sp = await res.json();

    return(
        <div>
            <h2>問題 {Number(idx)+1}</h2>
            <p>{sp.question}</p>
            <QuestionForm
                sessionId={sessionId}
                idx={Number(idx)}
                sessProbId={sp.id}
                choices={sp.choices}
                total={sp.total}
                />
        </div>
    )
}