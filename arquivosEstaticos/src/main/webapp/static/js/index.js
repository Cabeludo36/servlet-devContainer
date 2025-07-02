function buscarPessoas(idElementoPessoas) {
    const pessoasDiv = document.getElementById(idElementoPessoas);
    pessoasDiv.innerHTML = 'Loading...';
    fetch('api/pessoa')
        .then(response => response.json())
        .then(data => {
            const pessoasUl = document.createElement("ul");
            data.forEach(pessoa => {
                const li = document.createElement('li');
                li.innerHTML = `
                    <div id="pessoa-${pessoa.id}">
                        Name: ${pessoa.nome} - 
                        Age: ${pessoa.idade} - 
                        Can Drink: ${pessoa.podeBeber ? 'Yes' : 'No'} - 
                        Lucky Number: ${pessoa.numeroSorte} 
                    </div>`;
                pessoasUl.appendChild(li);
            });
            pessoasDiv.innerHTML = pessoasUl.outerHTML;
        });
}