//session/[sessionId]/problems
import React from `react`

export default async function ProblemPage({params}){
    const {sessionId} = params;

    const res=await fetch(
        `${process.env.NEXT_PUBLIC_API_BASE_URL}/session/${sessionId}/problems`,
        {cache: "no-store"}
    );


    if(!res.ok){
        return <p>エラー：AOIレスポンス {res.status}</p>;
    }

    const problems = await res.json();

    return (
        <div>
        <h2>Session {sessionId}の問題一覧</h2>
        <ul>
            {problems.map(sp=>(
                <li key={sp.id}>
                    ID:{sp.problem.id}
                    問題:{sp.problem.question}
                </li>
            ))}
        </ul>
       </div>
    ) ; 
}