using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace ${packageName}.Entities
{
<#if hasStateEnum>
    public enum ${stateEnum.name}
    {
<#list stateEnum.values as value>
        ${value.name},
</#list>
    }

</#if>
    [Table("${tableName}")]
    public class ${className}
    {
<#list attributes as attr>
        <#if attr.isPrimary>[Key]</#if>
        public ${attr.csharpType} ${attr.name} { get; set; }
        
</#list>
<#if hasStateEnum>
        public ${stateEnum.name} Status { get; set; }
        
</#if>
        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
        public DateTime UpdatedAt { get; set; } = DateTime.UtcNow;

<#if hasTransitionMethods>
<#list transitionMethods as method>
        public void ${method.name}()
        {
            // ${method.trigger}
            UpdatedAt = DateTime.UtcNow;
        }
        
</#list>
</#if>
    }
}